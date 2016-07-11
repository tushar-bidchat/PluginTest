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
    },
};
    
function buttonOneClicked() {
    // alert("Button One Clicked");
    // getCameraPicture();
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
}

app.initialize();