package com.kaspersky.kaspresso.interceptors.interactors.impl.autoscroll

import androidx.test.espresso.PerformException
import androidx.test.espresso.web.sugar.Web
import androidx.test.espresso.web.webdriver.DriverAtoms
import com.kaspersky.kaspresso.interceptors.interactors.AutoscrollInteractor
import com.kaspersky.kaspresso.interceptors.interactors.WebInteractor
import com.kaspersky.kaspresso.logger.UiTestLogger
import junit.framework.AssertionFailedError

class AutoscrollWebInteractor(
    private val logger: UiTestLogger
) : WebInteractor, AutoscrollInteractor<Web.WebInteraction<*>> {

    override fun <R> interact(interaction: Web.WebInteraction<*>, action: () -> R): R {
        return try {
            action.invoke()
        } catch (error: AssertionFailedError) {
            autoscroll(interaction, action, error)
        } catch (error: PerformException) {
            autoscroll(interaction, action, error)
        }
    }

    override fun <R> autoscroll(interaction: Web.WebInteraction<*>, action: () -> R, error: Throwable): R {
        val result: R = try {
            interaction.perform(DriverAtoms.webScrollIntoView())
            action.invoke()
        } catch (e: Throwable) {
            throw error
        }

        logger.i("Web autoscroll successfully performed.")
        return result
    }
}