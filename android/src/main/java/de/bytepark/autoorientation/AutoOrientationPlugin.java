package de.bytepark.autoorientation;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** AutoOrientationPlugin */
public class AutoOrientationPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  private static final String CHANNEL_NAME = "auto_orientation";
  private MethodChannel channel;
  private static Activity activity;

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    if (registrar.activity() != null) {
      AutoOrientationPlugin.activity = registrar.activity();
    }
    AutoOrientationPlugin plugin = new AutoOrientationPlugin();
    plugin.setupChannel(registrar.messenger(), registrar.context());
  }

  @Override
  public void onAttachedToEngine(FlutterPluginBinding binding) {
    setupChannel(binding.getFlutterEngine().getDartExecutor(), binding.getApplicationContext());
  }

  @Override
  public void onDetachedFromEngine(FlutterPluginBinding binding) {
    teardownChannel();
  }


  private void setupChannel(BinaryMessenger messenger, Context context) {
    channel = new MethodChannel(messenger, CHANNEL_NAME);
    channel.setMethodCallHandler(this);

  }

  private void teardownChannel() {
    channel.setMethodCallHandler(null);
    channel = null;
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (AutoOrientationPlugin.activity == null) {
      result.error("activity=null", "activity=null", 0);
      return;
    }
    switch(call.method) {
      case "setLandscapeRight":
        AutoOrientationPlugin.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        break;
      case "setLandscapeLeft":
        AutoOrientationPlugin.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        break;
      case "setPortraitUp":
        AutoOrientationPlugin.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        break;
      case "setPortraitDown":
        AutoOrientationPlugin.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        break;
      case "setPortraitAuto":
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
          AutoOrientationPlugin.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        } else {
          AutoOrientationPlugin.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
        break;
      case "setLandscapeAuto":
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
          AutoOrientationPlugin.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);
        } else {
          AutoOrientationPlugin.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
        break;
      case "setAuto":
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
          AutoOrientationPlugin.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_USER);
        } else {
          AutoOrientationPlugin.activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        }
        break;
      default:
        result.notImplemented();
        break;
    }
  }


  @Override
  public void onAttachedToActivity(ActivityPluginBinding binding)
  {
    AutoOrientationPlugin.activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    AutoOrientationPlugin.activity = null;
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
    AutoOrientationPlugin.activity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivity() {
    AutoOrientationPlugin.activity = null;
  }
}
