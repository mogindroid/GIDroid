// Generated by view binder compiler. Do not edit!
package com.aaronjwood.portauthority.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.aaronjwood.portauthority.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityWanhostBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final EditText hostAddress;

  @NonNull
  public final ListView portList;

  @NonNull
  public final TextView portListLabel;

  @NonNull
  public final Button scanPortRange;

  @NonNull
  public final Button scanWellKnownPorts;

  @NonNull
  public final TextView scanningDisclaimer;

  @NonNull
  public final TextView scanningWarning;

  private ActivityWanhostBinding(@NonNull RelativeLayout rootView, @NonNull EditText hostAddress,
      @NonNull ListView portList, @NonNull TextView portListLabel, @NonNull Button scanPortRange,
      @NonNull Button scanWellKnownPorts, @NonNull TextView scanningDisclaimer,
      @NonNull TextView scanningWarning) {
    this.rootView = rootView;
    this.hostAddress = hostAddress;
    this.portList = portList;
    this.portListLabel = portListLabel;
    this.scanPortRange = scanPortRange;
    this.scanWellKnownPorts = scanWellKnownPorts;
    this.scanningDisclaimer = scanningDisclaimer;
    this.scanningWarning = scanningWarning;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityWanhostBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityWanhostBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_wanhost, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityWanhostBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.hostAddress;
      EditText hostAddress = rootView.findViewById(id);
      if (hostAddress == null) {
        break missingId;
      }

      id = R.id.portList;
      ListView portList = rootView.findViewById(id);
      if (portList == null) {
        break missingId;
      }

      id = R.id.portListLabel;
      TextView portListLabel = rootView.findViewById(id);
      if (portListLabel == null) {
        break missingId;
      }

      id = R.id.scanPortRange;
      Button scanPortRange = rootView.findViewById(id);
      if (scanPortRange == null) {
        break missingId;
      }

      id = R.id.scanWellKnownPorts;
      Button scanWellKnownPorts = rootView.findViewById(id);
      if (scanWellKnownPorts == null) {
        break missingId;
      }

      id = R.id.scanningDisclaimer;
      TextView scanningDisclaimer = rootView.findViewById(id);
      if (scanningDisclaimer == null) {
        break missingId;
      }

      id = R.id.scanningWarning;
      TextView scanningWarning = rootView.findViewById(id);
      if (scanningWarning == null) {
        break missingId;
      }

      return new ActivityWanhostBinding((RelativeLayout) rootView, hostAddress, portList,
          portListLabel, scanPortRange, scanWellKnownPorts, scanningDisclaimer, scanningWarning);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}