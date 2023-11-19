package dev.ahmedmourad.showcase.common.ios

import androidx.compose.ui.graphics.Color
import dev.ahmedmourad.showcase.common.utils.Message
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIColor
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIStoryboardSegue
import platform.UIKit.UIView
import platform.UIKit.UIViewController
import platform.UIKit.addChildViewController
import platform.UIKit.didMoveToParentViewController
import kotlin.time.Duration.Companion.seconds

interface UIViewControllerDelegate {
    val statusBarStyle: UIStatusBarStyle
    fun onViewDidLoad(self: UIViewController) { }
    fun onViewWillAppear(self: UIViewController) { }
    fun onViewWillDisappear(self: UIViewController) { }
    fun prepareForSegue(self: UIViewController, segue: UIStoryboardSegue, sender: Any?) { }
    fun willMove(to: UIViewController?) { }
}

@OptIn(ExperimentalForeignApi::class)
fun UIViewController.embed(controller: UIViewController, into: UIView) {
    this.addChildViewController(controller)
    into.addSubview(controller.view)
    controller.view.setFrame(into.bounds)
    controller.didMoveToParentViewController(this)
}

suspend fun UIViewController.toast(message: Message, seconds: Double = 2.0) {
    this.toast(message.v, seconds)
}

suspend fun UIViewController.toast(message: String, seconds: Double = 2.0) = coroutineScope {
    val alert = UIAlertController.alertControllerWithTitle(
        title = null,
        message = message,
        preferredStyle = UIAlertControllerStyleAlert
    )
    alert.view.backgroundColor = UIColor.blackColor
    alert.view.alpha = 0.6
    alert.view.layer.cornerRadius = 15.0
    this@toast.presentViewController(alert, animated = true, completion = null)
    launch {
        delay(seconds.seconds)
        alert.dismissModalViewControllerAnimated(animated = true)
    }
}

fun Color.toUIColor() = UIColor(
    red = this.red.toDouble(),
    green = this.green.toDouble(),
    blue = this.blue.toDouble(),
    alpha = this.alpha.toDouble()
)
