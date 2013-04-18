package kandidat.nfc.nfcapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class KeysDialogDelete extends DialogFragment {

	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Delete");
			builder.setMessage("Are you sure?");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//finish() MainActvity
				}
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// ignore
				}
			});
		return builder.create();
	}



	public static interface KeysDialogInterface {
		public void onChoose();
	}

	@SuppressWarnings("unused")
	private KeysDialogInterface mListener;

	@Override
	public void onAttach(Activity activity) {
		mListener = (KeysDialogInterface) activity;
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		mListener = null;
		super.onDetach();
	}
}
