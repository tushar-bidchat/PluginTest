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
//    alert("Button One Clicked");
//    cordova.InAppBrowser.open('http://apache.org', '_blank', 'location=yes');
    //Using popup (option 1)
    OAuth.initialize('YnytujS-j6aFzEoeUDc6vnnTwhE');
    OAuth.popup('facebook')
    .done(function(result) {
      //use result.access_token in your API request 
      //or use result.get|post|put|del|patch|me methods (see below)
    })
    .fail(function (err) {
      //handle error with err
    });
}

app.initialize();