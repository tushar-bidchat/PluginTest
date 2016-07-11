cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/bidchat-plugin-camerapreview/www/CameraPreview.js",
        "id": "bidchat-plugin-camerapreview.CameraPreview",
        "pluginId": "bidchat-plugin-camerapreview",
        "clobbers": [
            "cordova.plugins.camerapreview"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-whitelist": "1.2.2",
    "bidchat-plugin-camerapreview": "1.0.0"
}
// BOTTOM OF METADATA
});