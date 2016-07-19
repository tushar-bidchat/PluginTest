var app = {
    
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    
    // Bind Event Listeners
    // Bind any events that are required on startup. 
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
        document.getElementById('first_button').addEventListener('click', buttonOneClicked, false);
    },
    
    // deviceready Event Handler
    onDeviceReady: function() {
        console.log("onDeviceReady");
        console.log(navigator.compass);
    },
};
    
function buttonOneClicked() {
    // alert("Button One Clicked");
    // getCameraPicture();
    // getDeviceInfo();
    // getDeviceOrientation();
    
    // showInformationMessage('Button Clicked Natively !');
    
    // showCurrentGeoLocation();
    
//    showIsWifiAvailable();
    
    isAuthorizedForContacts();
}

function showInformationMessage(message) {
    
    var okPressed = function() {
        console.log('Ok Pressed');
    }
    
    navigator.notification.alert(
    message,        // message
    okPressed,      // callback
    'Information',  // title
    'Ok'            // buttonName
    );
}

// cordova-diagnostic-plugin Usage 
function showIsWifiAvailable() {
    var onSuccess = function(message) {
        alert('Sucess : Wifi Available ' + message);
    }
    
    var onError = function(message) {
        alert('Error: ' + message.code);
    }
    
    cordova.plugins.diagnostic.isWifiAvailable(onSuccess, onError);
}


function isAuthorizedForContacts() {
    cordova.plugins.diagnostic.isContactsAuthorized(
        function(authorized){
            alert("App has " + (authorized ? "authorized" : "denied") + " access to contacts");
        }, 
        function(error){
            alert("The following error occurred: "+error);
        }
    );
}

function showCurrentGeoLocation () {
    
    // onSuccess Callback
    // This method accepts a Position object, which contains the
    // current GPS coordinates
    //
    var onSuccess = function(position) {
        alert('Latitude: '          + position.coords.latitude          + '\n' +
              'Longitude: '         + position.coords.longitude         + '\n' +
              'Altitude: '          + position.coords.altitude          + '\n' +
              'Accuracy: '          + position.coords.accuracy          + '\n' +
              'Altitude Accuracy: ' + position.coords.altitudeAccuracy  + '\n' +
              'Heading: '           + position.coords.heading           + '\n' +
              'Speed: '             + position.coords.speed             + '\n' +
              'Timestamp: '         + position.timestamp                + '\n');
    };

    // onError Callback receives a PositionError object
    //
    function onError(error) {
        alert('code: '    + error.code    + '\n' +
              'message: ' + error.message + '\n');
    }

    navigator.geolocation.getCurrentPosition(onSuccess, onError);
}
/**
 * Default Implemenetion of Capture Picture from Camera Gallery
 */      
function getCameraPicture() {
    
    var cameraSourceType = navigator.camera.PictureSourceType;
    var cameraSourceTypeSaved = cameraSourceType.SAVEDPHOTOALBUM; // {PHOTOLIBRARY: 0, CAMERA: 1, SAVEDPHOTOALBUM: 2} = $1
    var destinationType = navigator.camera.DestinationType; // {DATA_URL: 0, FILE_URI: 1, NATIVE_URI: 2}
    
    var options = {
        quality: 50,
        destinationType: destinationType.FILE_URI,
        correctOrientation: true,
        sourceType: cameraSourceTypeSaved
    }
       
    var onSuccess = function(result) {
        console.log("Share completed? " + result.completed); // On Android apps mostly return false even while it's true
        console.log("Shared to app: " + result.app); // On Android result.app is currently empty. On iOS it's empty when sharing is cancelled (result.completed=false)
    }

    var onError = function(msg) {
        console.log("Sharing failed with message: " + msg);
    }

    navigator.camera.getPicture(onSuccess, onError, options);
    
    navigator.camera.takePicture
}

function handleOpenURL(url) {
  setTimeout(function() {
    alert("received url: " + url);
  }, 0);
}

/**
 * Provides device information
 **/  
function getDeviceInfo() {
    console.log("Platform = " + device.platform);
    console.log("UUID = " + device.uuid);
    console.log("Model = " + device.model);
    console.log("Version = " + device.version);
}

function getDeviceOrientation() {
    
    var onSuccess = function(heading) {
        alert('Heading: ' + heading.magneticHeading);
    }
    
    var onError = function(heading) {
        alert('CompassError: ' + error.code);
    }
    
    navigator.compass.getCurrentHeading(onSuccess, onError);
}

app.initialize();