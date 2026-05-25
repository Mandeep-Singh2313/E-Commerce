package com.project.ecommerce.service;

import com.project.ecommerce.payload.StripePaymentDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface StripeService {


    PaymentIntent paymentIntent(StripePaymentDto stripePaymentDto) throws StripeException;
}
