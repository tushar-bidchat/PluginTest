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

function buttonOneClicked() {
    alert("Button One Clicked");
    showShareSheet();                                                    
}

/**
 * Default Implemenetion of Share Sheet
 */      
function showShareSheet() {
    // this is the complete list of currently supported params you can pass to the plugin (all optional)
    var options = {
        message: 'share this', // not supported on some apps (Facebook, Instagram)
        subject: 'the subject', // fi. for email
        files: ['', ''], // an array of filenames either locally or remotely
        url: 'https://www.website.com/foo/#bar?a=b',
        chooserTitle: 'Pick an app' // Android only, you can override the default share sheet title
    }
        
    var onSuccess = function(result) {
        console.log("Share completed? " + result.completed); // On Android apps mostly return false even while it's true
        console.log("Shared to app: " + result.app); // On Android result.app is currently empty. On iOS it's empty when sharing is cancelled (result.completed=false)
    }

    var onError = function(msg) {
        console.log("Sharing failed with message: " + msg);
    }

    window.plugins.socialsharing.shareWithOptions(options, onSuccess, onError);   
}

app.initialize();