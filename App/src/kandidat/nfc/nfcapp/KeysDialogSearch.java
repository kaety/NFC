package kandidat.nfc.nfcapp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class KeysDialogSearch extends DialogFragment {

	@Override
	public AlertDialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Search");
			builder.setMessage("Custom dialog for search");
			builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//finish() MainActvity
				}
			});
			builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// ignore
				}
			});
		return builder.create();
	}
}