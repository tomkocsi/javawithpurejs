function handlePasswordChange() {
    if (document.getElementById("password").value ==
            document.getElementById("confirm_password").value) {
        document.getElementById("submit").disabled = false;
		document.getElementById("notmatchingpassword").innerHTML = "Jelszónak egyezni kell!";
    } else {
        document.getElementById("submitreg").disabled = true;
    	document.getElementById("notmatchingpassword").innerHTML = "";
    
	}
}