package com.sirdave.buildspace.presentation.subscription

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sirdave.buildspace.data.local.AuthManager
import com.sirdave.buildspace.domain.model.SubscriptionHistory
import com.sirdave.buildspace.domain.model.SubscriptionPlan
import com.sirdave.buildspace.domain.model.User
import com.sirdave.buildspace.domain.repository.SubscriptionRepository
import com.sirdave.buildspace.domain.use_cases.ValidateField
import com.sirdave.buildspace.presentation.ErrorEvent
import com.sirdave.buildspace.presentation.credit_card.CardDetailsState
import com.sirdave.buildspace.presentation.credit_card.CardEvent
import com.sirdave.buildspace.presentation.payment.PaymentEvent
import com.sirdave.buildspace.presentation.payment.PaymentState
import com.sirdave.buildspace.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val repository: SubscriptionRepository,
    private val authManager: AuthManager,
    private val validateField: ValidateField
): ViewModel() {

    var user by mutableStateOf<User?>(null)

    private val _subscriptionState = MutableStateFlow(SubscriptionState())
    val subscriptionState: StateFlow<SubscriptionState> = _subscriptionState

    var cardDetailsState by mutableStateOf(CardDetailsState())

    var paymentState by mutableStateOf(PaymentState())

    private val errorEventChannel = Channel<ErrorEvent>()
    val errorEvent = errorEventChannel.receiveAsFlow()

    private var subscriptionHistory = listOf<SubscriptionHistory>()

    init {
        viewModelScope.launch {
            val userResult = async { authManager.getUser().first() }
            user = userResult.await()
        }
    }

    private suspend fun <T> triggerUserLogout(result: Resource.Error<T>){
        if (result.message == "Token has expired, login to continue" ||
            result.message == "You need to log in to access this page")
            errorEventChannel.send(ErrorEvent.UserNeedsToLoginEvent)
    }
    private fun getCurrentSubscription(userId: String, fetchFromRemote: Boolean = false){
        _subscriptionState.value = _subscriptionState.value.copy(isLoading = true)
        viewModelScope.launch {
            val currentSubscriptionResult = repository.getUserCurrentSubscription(userId, fetchFromRemote)
            currentSubscriptionResult.collect{
                withContext(Dispatchers.Main){
                    when (val result = it) {

                        is Resource.Success -> {
                            _subscriptionState.value = _subscriptionState.value.copy(
                                isLoading = false,
                                error = null,
                                currentSubscription = result.data!!
                            )
                        }

                        is Resource.Error -> {
                            _subscriptionState.value = _subscriptionState.value.copy(
                                isLoading = false,
                                error = result.message,
                                currentSubscription = null
                            )

                            triggerUserLogout(result)
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    private fun getTransactionHistory(userEmail: String, fetchFromRemote: Boolean = false) {
        _subscriptionState.value = _subscriptionState.value.copy(isLoading = true)
        viewModelScope.launch {
            val subscriptionHistoryResult =
                repository.getUserTransactionHistory(userEmail, fetchFromRemote)

            subscriptionHistoryResult.collect {
                withContext(Dispatchers.Main) {
                    when (val result = it) {

                        is Resource.Success -> {
                            _subscriptionState.value = _subscriptionState.value.copy(
                                isLoading = false,
                                error = null,
                                subscriptionList = result.data!!
                            )
                            subscriptionHistory = result.data
                        }

                        is Resource.Error -> {
                            _subscriptionState.value = _subscriptionState.value.copy(
                                isLoading = false,
                                error = result.message,
                                subscriptionList = emptyList()
                            )

                            triggerUserLogout(result)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun getSubscriptionPlans(type: String, fetchFromRemote: Boolean = false){
        _subscriptionState.value = _subscriptionState.value.copy(isLoading = true)
        viewModelScope.launch {
            val subscriptionPlansResult = repository.getSubscriptionPlans(type, fetchFromRemote)
            subscriptionPlansResult.collect{
                withContext(Dispatchers.Main){
                    when (val result = it) {

                        is Resource.Success -> {
                            _subscriptionState.value = _subscriptionState.value.copy(
                                isLoading = false,
                                error = null,
                                subscriptionPlans = result.data!!
                            )
                        }

                        is Resource.Error -> {
                            _subscriptionState.value = _subscriptionState.value.copy(
                                isLoading = false,
                                error = result.message,
                                subscriptionPlans = emptyList()
                            )
                            triggerUserLogout(result)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    fun onEvent(event: CardEvent){
        when (event){
            is CardEvent.CardNumberChanged -> {
                cardDetailsState = cardDetailsState.copy(cardNumber = event.number)
            }
            is CardEvent.CardExpiryDateChanged -> {
                cardDetailsState = cardDetailsState.copy(cardExpiryDate = event.expiryDate)
            }
            is CardEvent.CardCVCChanged -> {
                cardDetailsState = cardDetailsState.copy(cardCVV = event.cvc)
            }

            is CardEvent.CardPinChanged ->{
                cardDetailsState = cardDetailsState.copy(cardPin = event.pin)
            }

            is CardEvent.CardOTPChanged -> {
                cardDetailsState = cardDetailsState.copy(cardOTP = event.otp)
            }

            is CardEvent.Submit -> {
                validateCardDetails(event.plan)
            }

            is CardEvent.SendOTP ->{
                submitOTP()
            }
        }
    }

    fun onSubscriptionEvent(event: SubscriptionEvent){
        when(event){
            is SubscriptionEvent.GetCurrentSubscription -> {
                user?.let { getCurrentSubscription(it.id) }
            }
            is SubscriptionEvent.RefreshCurrentSubscription -> {
                user?.let { getCurrentSubscription(it.id, true) }
            }

            is SubscriptionEvent.GetIndividualPlans -> getSubscriptionPlans("individual")

            is SubscriptionEvent.GetTeamPlans -> getSubscriptionPlans("team")

            is SubscriptionEvent.GetHistory -> {
                user?.let { getTransactionHistory(it.email) }
            }

            is SubscriptionEvent.RefreshHistory -> {
                user?.let { getTransactionHistory(it.email, true) }
            }
            is SubscriptionEvent.FilterHistory -> {
                val filtered = if (event.planType.isBlank()) subscriptionHistory
                else subscriptionHistory.filter { it.subscriptionType.equals(event.planType, ignoreCase = true) }

                _subscriptionState.value = _subscriptionState.value.copy(
                    subscriptionList = filtered
                )
            }

            is SubscriptionEvent.RefreshAll -> {
                user?.let {
                    getCurrentSubscription(it.id, true)
                    getTransactionHistory(it.email, true)
                }
            }
        }
    }

    fun onPaymentEvent(event: PaymentEvent){
        paymentState = when(event){
            is PaymentEvent.ResetPaymentError -> {
                paymentState.copy(error = null)
            }

            is PaymentEvent.ResetPaymentMessage -> {
                paymentState.copy(message = null)
            }
        }
    }

    private fun validateCardDetails(plan: SubscriptionPlan){
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

        val month = cardDetailsState.cardExpiryDate.substring(0, 2)
        val year = cardDetailsState.cardExpiryDate.substring(2)

        createSubscription(
            email = user?.email ?: "",
            amount = stripFormatting(plan.amount).toDouble(),
            cardCvv = cardDetailsState.cardCVV,
            cardNumber = cardDetailsState.cardNumber,
            cardExpiryMonth = month,
            cardExpiryYear = year,
            pin = cardDetailsState.cardPin,
            name = plan.name
        )
    }
    private fun createSubscription(email: String, amount: Double, cardCvv: String,
                                   cardNumber: String, cardExpiryMonth: String,
                                   cardExpiryYear: String, pin: String, name: String){
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
                name = name
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

    private fun stripFormatting(input: String): String{
        return input.replace(Regex("\\D"), "")
    }
}