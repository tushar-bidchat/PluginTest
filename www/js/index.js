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
        app.receivedEvent('deviceready');
    },
    
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

function successCameraStart(callback){
}
function failureCameraStart(callback){
}

function buttonOneClicked(){
  var tapEnabled = true; //enable tap take picture
  var dragEnabled = true; //enable preview box drag across the screen
  var toBack = true; //send preview box to the back of the webview
  var rect = {x: 100, y: 100, width: 200, height:200};
  var alpha = 1;
  cordova.plugins.camerapreview.startCamera(rect, "back", tapEnabled, dragEnabled, toBack, alpha, successCameraStart, failureCameraStart);
}

app.initialize();