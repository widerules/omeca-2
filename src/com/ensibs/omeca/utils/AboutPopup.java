package com.ensibs.omeca.utils;

import android.app.AlertDialog;
import android.content.Context;

public class AboutPopup {

	public static void show(Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder
			.setMessage("OMECA version 2.0 (2013-2014)\n\nR�alis�e par:\n\t- Rapha�l GICQUIAUX\n\t- Nicolas HALLOUIN\n\t- Sylvain RIO\n\t- Lindsay ROZIER\ndans le cadre du projet de fin d'�tudes de l'ENSIbs")
			.show();

	}

}
