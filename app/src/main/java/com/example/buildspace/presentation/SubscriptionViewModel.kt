package com.example.buildspace.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buildspace.data.local.AuthManager
import com.example.buildspace.data.local.BuildSpaceDatabase
import com.example.buildspace.data.mapper.*
import com.example.buildspace.domain.model.SubscriptionPlan
import com.example.buildspace.domain.model.User
import com.example.buildspace.domain.repository.SubscriptionRepository
import com.example.buildspace.domain.use_cases.ValidateField
import com.example.buildspace.presentation.credit_card.*
import com.example.buildspace.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    private val authManager: AuthManager,
    private val validateField: ValidateField,
    db: BuildSpaceDatabase
): ViewModel() {

    private val dao = db.dao
    private val TAG = "SubscriptionViewModel"

    var user by mutableStateOf<User?>(null)

    private val _subscriptionState = MutableStateFlow(SubscriptionState())
    val subscriptionState: StateFlow<SubscriptionState> = _subscriptionState

    var cardDetailsState by mutableStateOf(CardDetailsState())

    var paymentState by mutableStateOf(PaymentState())

    init {
        viewModelScope.launch {

            coroutineScope {
                val userResult = async { authManager.getUser().first() }
                user = userResult.await()
            }

            _subscriptionState.value = _subscriptionState.value.copy(isLoading = true)

            val localCurrentSubscription = dao.getCurrentSubscription()
            val localSubscriptionHistory = dao.getSubscriptionHistory()
            val localSubscriptionPlans = dao.getSubscriptionPlans()

            val currentTimeMillis = System.currentTimeMillis()
            val cacheExpiryDurationMillis = 24 * 60 * 60 * 1000
            val isCacheValid = localCurrentSubscription?.let {
                (currentTimeMillis - it.timestamp) < cacheExpiryDurationMillis
            } ?: false

            if (isCacheValid) {
                Log.d(TAG, "fetching from DB")
                _subscriptionState.value = _subscriptionState.value.copy(
                    isLoading = false,
                    error = null,
                    currentSubscription = localCurrentSubscription?.toSubscription(),
                    subscriptionList = localSubscriptionHistory.map { history -> history.toSubscriptionHistory() },
                    subscriptionPlans = localSubscriptionPlans.map { it.toSubscriptionPlan() }
                )
                return@launch
            }

            user?.let {
                Log.d(TAG, "fetching from remote")
                val currentSubscriptionDeferred = async { repository.getUserCurrentSubscription(it.id) }
                val subscriptionHistoryDeferred = async { repository.getUserTransactionHistory(it.email) }
                val subscriptionPlansDeferred = async { repository.getAllSubscriptionPlans() }

                val currentSubscriptionResult = currentSubscriptionDeferred.await()
                val subscriptionHistoryResult = subscriptionHistoryDeferred.await()
                val subscriptionPlansResult = subscriptionPlansDeferred.await()

                currentSubscriptionResult.collect{
                    withContext(Dispatchers.Main){
                        when (val result = it) {

                            is Resource.Success -> {
                                _subscriptionState.value = _subscriptionState.value.copy(
                                    isLoading = false,
                                    error = null,
                                    currentSubscription = result.data!!.toSubscription()
                                )
                                dao.clearSubscription()
                                dao.insertSubscription(
                                    result.data.toSubscriptionEntity(System.currentTimeMillis())
                                )
                            }

                            is Resource.Error -> {
                                _subscriptionState.value = _subscriptionState.value.copy(
                                    isLoading = false,
                                    error = result.message,
                                    currentSubscription = null
                                )
                            }

                            else -> Unit
                        }
                    }
                }

                subscriptionHistoryResult.collect{
                    withContext(Dispatchers.Main){
                        when (val result = it) {

                            is Resource.Success -> {
                                _subscriptionState.value = _subscriptionState.value.copy(
                                    isLoading = false,
                                    error = null,
                                    subscriptionList = result.data!!.map { history ->  history.toSubscriptionHistory() }
                                )
                                dao.clearHistory()
                                dao.insertSubscriptionHistory(result.data.map { it.toSubscriptionHistoryEntity() })
                            }

                            is Resource.Error -> {
                                _subscriptionState.value = _subscriptionState.value.copy(
                                    isLoading = false,
                                    error = result.message,
                                    subscriptionList = emptyList()
                                )
                            }
                            else -> Unit
                        }
                    }
                }
                subscriptionPlansResult.collect{
                    withContext(Dispatchers.Main){
                        when (val result = it) {

                            is Resource.Success -> {
                                _subscriptionState.value = _subscriptionState.value.copy(
                                    isLoading = false,
                                    error = null,
                                    subscriptionPlans = result.data!!.map { it.toSubscriptionPlan() }
                                )
                                dao.clearPlans()
                                dao.insertSubscriptionPlans(result.data.map { it.toSubscriptionPlanEntity() })
                            }

                            is Resource.Error -> {
                                _subscriptionState.value = _subscriptionState.value.copy(
                                    isLoading = false,
                                    error = result.message,
                                    subscriptionPlans = emptyList()
                                )
                            }
                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    fun onEvent(event: CardEvent){
        when (event){
            is CardEvent.CardNumberChanged -> {
                val formattedNumber = formatCardNumber(event.number)
                cardDetailsState = cardDetailsState.copy(cardNumber = formattedNumber)
            }
            is CardEvent.CardExpiryDateChanged -> {
                val formattedDate = formatExpirationDate(event.expiryDate)
                cardDetailsState = cardDetailsState.copy(cardExpiryDate = formattedDate)
            }
            is CardEvent.CardCVCChanged -> {
                val formattedCVV = formatCvv(event.cvc)
                cardDetailsState = cardDetailsState.copy(cardCVV = formattedCVV)
            }

            is CardEvent.CardPinChanged ->{
                val formattedPin = formatPin(event.pin)
                cardDetailsState = cardDetailsState.copy(cardPin = formattedPin)
            }

            is CardEvent.CardOTPChanged -> {
                cardDetailsState = cardDetailsState.copy(cardOTP = event.otp)
            }

            is CardEvent.Submit -> {
                handleData(event.plan)
            }

            is CardEvent.SendOTP ->{
                submitOTP()
            }
        }
    }

    private fun handleData(plan: SubscriptionPlan){
        val cardNumberResult = validateField.execute(cardDetailsState.cardNumber)
        val cardExpiryDateResult = validateField.execute(cardDetailsState.cardExpiryDate)
        val cardCVVResult = validateField.execute(cardDetailsState.cardCVV)
        val cardPinResult = validateField.execute(cardDetailsState.cardPin)

        val hasError = listOf(
            cardNumberResult,
            cardExpiryDateResult,
            cardCVVResult,
            cardPinResult
        ).any{ !it.isSuccessful}

        if (hasError){
            cardDetailsState = cardDetailsState.copy(
                cardNumberError = cardNumberResult.errorMessage,
                cardExpiryDateError = cardExpiryDateResult.errorMessage,
                cardCVVError = cardExpiryDateResult.errorMessage,
                cardPinError = cardPinResult.errorMessage
            )
            return
        }

        val month = getMonthAndYear(cardDetailsState.cardExpiryDate).first
        val year = getMonthAndYear(cardDetailsState.cardExpiryDate).second

        createSubscription(
            email = user?.email ?: "",
            amount = plan.amount.toDouble(),
            cardCvv = stripFormatting(cardDetailsState.cardCVV),
            cardNumber = stripFormatting(cardDetailsState.cardNumber),
            cardExpiryMonth = month,
            cardExpiryYear = year,
            pin = stripFormatting(cardDetailsState.cardPin),
            type = plan.name
        )
    }
    private fun createSubscription(email: String, amount: Double, cardCvv: String,
                                   cardNumber: String, cardExpiryMonth: String,
                                   cardExpiryYear: String, pin: String, type: String){
        paymentState = paymentState.copy(isPaymentLoading = true)
        viewModelScope.launch {
            repository.createSubscription(
                email = email,
                amount = amount,
                cardCvv = cardCvv,
                cardNumber = cardNumber,
                cardExpiryMonth = cardExpiryMonth,
                cardExpiryYear = cardExpiryYear,
                pin = pin,
                type = type
            ).collect{
                withContext(Dispatchers.Main){
                    when (val result = it){
                        is Resource.Success ->{
                            paymentState = paymentState.copy(
                                isPaymentLoading = false,
                                error = null,
                                message = it.data?.data?.status,
                                reference = it.data?.data?.reference
                            )
                        }

                        is Resource.Error ->{
                            paymentState = paymentState.copy(
                                isPaymentLoading = false,
                                error = result.message
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
    private fun submitOTP() {
        val cardOTPResult = validateField.execute(cardDetailsState.cardOTP)
        val hasError = !cardOTPResult.isSuccessful
        if (hasError){
            cardDetailsState = cardDetailsState.copy(
                cardOTPError = cardOTPResult.errorMessage
            )
            return
        }

        paymentState = paymentState.copy(isPaymentLoading = true)
        viewModelScope.launch {
            repository.sendOTP(
                otp = cardDetailsState.cardOTP,
                reference = paymentState.reference ?: ""
            ).collect{
                withContext(Dispatchers.Main){
                    when (val result = it){
                        is Resource.Success ->{
                            paymentState = paymentState.copy(
                                isPaymentLoading = false,
                                error = null,
                                message = it.data?.data?.status
                            )
                        }

                        is Resource.Error ->{
                            paymentState = paymentState.copy(
                                isPaymentLoading = false,
                                error = result.message
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}