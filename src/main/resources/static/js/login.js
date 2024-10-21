function handleUsernameOrPasswordChange(){
    document.getElementById("paragloginwarning").innerHTML = "";
}
function handlePasswordChange() {
    if (document.getElementById("regpassword").value ==
            document.getElementById("cregpassword").value) {
        document.getElementById("notmatchingpassword").innerHTML = "";
    } else {
        document.getElementById("notmatchingpassword").innerHTML = "Jelszónak egyezni kell!";
    }
}
async function populateUsernameFromResponse(responseinfo){
	const userdto = await responseinfo;
	const loginusername = document.getElementById("username");
	loginusername.value = userdto.email;
	clearRegistrationModalElements();
    $("#regModal").modal("hide");
	loginusername.value = userdto.email;
}
async function uploadUserDataFromForm(formdata, doWhenUploaded){
	let req = new Request("http://localhost:9876/api/registrate",
        {
            method: "POST",
            headers: {
               'Accept': 'application/json'
            },
            body: formdata
        }
    );
    fetch(req).then(
        function(response){
            //put response data on chain
            return response.json();
        }).then(
        function(json){
            // use response data
            const responseinfo = {
                id: json.id,
                email: json.email,
            	message: json.message
			}
            doWhenUploaded(responseinfo);
        
        }).catch(
        function(err){
            console.log("Fetch problem: " + err.message);
        }
    );
}
function handleOpenRegistration(){
	const regModal = document.getElementById("regModal");
	let BSregModal = new bootstrap.Modal(regModal, {backdrop: "static"});
    BSregModal.show();
}

function handleRegistrate(){
	if (document.getElementById("regpassword").value != undefined && 
		document.getElementById("regpassword").value.length > 0 &&
        document.getElementById("regpassword").value ==
		    document.getElementById("cregpassword").value) {
        document.getElementById("notmatchingpassword").innerHTML = "";
    	modalform = document.getElementById("regform");
    	const useremail = modalform.useremail.value;
		let formData = new FormData();
		formData.append("useremail", useremail);
		formData.append("password", modalform.password.value);
		//AJAX call
		uploadUserDataFromForm(formData, populateUsernameFromResponse);
	} else {
        document.getElementById("notmatchingpassword").innerHTML = "Jelszónak egyezni kell!";
    }
}

function clearRegistrationModalElements(){
	 document.querySelectorAll("#regModal input[type=text]").forEach(elem =>{
        elem.value="";
    });
    document.querySelectorAll("#regModal input[type=password]").forEach(elem =>{
		elem.value="";
	});
	document.getElementById("feedbackdiv").innerHTML = "";
	document.getElementById("feedbackdiv").innerHTML = "";
}

const btncloseregmodal = document.getElementById("btnCloseRegModal");
btncloseregmodal.addEventListener('click', function(){
    clearRegistrationModalElements();
    $("#regModal").modal("hide");
}, false);