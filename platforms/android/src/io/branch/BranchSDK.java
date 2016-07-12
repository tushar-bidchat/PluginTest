package io.branch;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import android.net.Uri;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.ShareSheetStyle;

public class BranchSDK extends CordovaPlugin
{

    static class BranchLinkProperties extends io.branch.referral.util.LinkProperties {}

    // Standard Debugging Variables
    private static final String LCAT = "CordovaBranchSDK";

    // Private Method Properties
    private ArrayList<BranchUniversalObjectWrapper> branchObjectWrappers;
    private Activity activity;
    private Branch instance;
    private String deepLinkUrl;

    /**
     * Class Constructor
     */
    public BranchSDK()
    {
        this.activity = null;
        this.instance = null;
        this.branchObjectWrappers = new ArrayList<BranchUniversalObjectWrapper>();
    }

    /**
     * Called when the activity receives a new intent.
     */
    public void onNewIntent(Intent intent)
    {        
        this.activity = this.cordova.getActivity();
        this.activity.setIntent(intent);

        if (this.activity != null) {
            this.initSession(null);
        }
    }

    /**
     * Called when the activity will start interacting with the user.
     *
     * @param multitasking A {@link boolean} flag indicating if multitasking is turned on for app
     */
    @Override
    public void onResume(boolean multitasking) {

    }

    /**
     * Called when the activity is no longer visible to the user.
     */
    @Override
    public void onStop()
    {

        if (this.instance != null) {
            this.instance.closeSession();
        }

    }

    /**
     * <p>
     * cordova.exec() method reference.
     * All exec() calls goes to this part.
     * </p>
     *
     * @param  action A {@link String} value method to be executed.
     * @param  args   A {@link JSONArray} value parameters passed along with the action param.
     * @param  callbackContext A {@link CallbackContext} function passed for executing callbacks.
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException
    {
        
        Runnable r = new RunnableThread(action, args, callbackContext);

        if (action.equals("setDebug")) {
            if (args.length() == 1) {
                cordova.getThreadPool().execute(r);
            }
            return true;
        } else if (action.equals("initSession")) {
            cordova.getThreadPool().execute(r);
            return true;
        } else {
            if (this.instance != null) {
                if (action.equals("setIdentity")) {
                    cordova.getThreadPool().execute(r);
                    return true;
                } else if (action.equals("userCompletedAction")) {
                    if (args.length() < 1 && args.length() > 2) {
                        callbackContext.error(String.format("Parameter mismatched. 1-2 is required but %d is given", args.length()));
                        return false;
                    }
                    cordova.getThreadPool().execute(r);
                    return true;
                } else if (action.equals("getFirstReferringParams")) {
                    cordova.getThreadPool().execute(r);
                    return true;
                } else if (action.equals("getLatestReferringParams")) {
                    cordova.getThreadPool().execute(r);
                    return true;
                } else if (action.equals("logout")) {
                    cordova.getThreadPool().execute(r);
                    return true;
                } else if (action.equals("loadRewards")) {
                    cordova.getThreadPool().execute(r);
                    return true;
                } else if (action.equals("redeemRewards")) {
                    if (args.length() < 1 && args.length() > 2) {
                        callbackContext.error(String.format("Parameter mismatched. 1-2 is required but %d is given", args.length()));
                        return false;
                    }
                    cordova.getThreadPool().execute(r);
                    return true;
                } else if (action.equals("getCreditHistory")) {
                    cordova.getThreadPool().execute(r);
                    return true;
                } else if (action.equals("createBranchUniversalObject")) {
                    if (args.length() != 1) {
                        callbackContext.error(String.format("Parameter mismatched. 1 is required but %d is given", args.length()));
                        return false;
                    }
                    cordova.getThreadPool().execute(r);
                    return true;
                } else if (action.equals(("generateShortUrl"))) {
                    if (args.length() != 3) {
                        callbackContext.error(String.format("Parameter mismatched. 3 is required but %d is given", args.length()));
                        return false;
                    }
                    cordova.getThreadPool().execute(r);
                    return true;
                } else if (action.equals("registerView")) {
                    if (args.length() != 1) {
                        callbackContext.error(String.format("Parameter mismatched. 1 is required but %d is given", args.length()));
                        return false;
                    }
                    cordova.getThreadPool().execute(r);
                    return true;
                } else if (action.equals("showShareSheet")) {
                    if (args.length() < 3) {
                        callbackContext.error(String.format("Parameter mismatched. 3 is required but %d is given", args.length()));
                        return false;
                    }
                    cordova.getActivity().runOnUiThread(r);
                    return true;
                } else if (action.equals("onShareLinkDialogLaunched")) {

                    BranchUniversalObjectWrapper branchObjWrapper = (BranchUniversalObjectWrapper)branchObjectWrappers.get(args.getInt(0));
                                           branchObjWrapper.onShareLinkDialogLaunched = callbackContext;

                    branchObjectWrappers.set(args.getInt(0), branchObjWrapper);

                } else if (action.equals("onShareLinkDialogDismissed")) {

                    BranchUniversalObjectWrapper branchObjWrapper = (BranchUniversalObjectWrapper)branchObjectWrappers.get(args.getInt(0));
                                           branchObjWrapper.onShareLinkDialogDismissed = callbackContext;

                    branchObjectWrappers.set(args.getInt(0), branchObjWrapper);

                } else if (action.equals("onLinkShareResponse")) {

                    BranchUniversalObjectWrapper branchObjWrapper = (BranchUniversalObjectWrapper)branchObjectWrappers.get(args.getInt(0));
                                           branchObjWrapper.onLinkShareResponse = callbackContext;

                    branchObjectWrappers.set(args.getInt(0), branchObjWrapper);

                } else if (action.equals("onChannelSelected")) {

                    BranchUniversalObjectWrapper branchObjWrapper = (BranchUniversalObjectWrapper)branchObjectWrappers.get(args.getInt(0));
                                           branchObjWrapper.onChannelSelected = callbackContext;

                    branchObjectWrappers.set(args.getInt(0), branchObjWrapper);

                }

                return true;

            } else {
                callbackContext.error("Branch instance not set. Please execute initSession() first.");
            }
        }

        return false;

    }

    //////////////////////////////////////////////////
    //----------- CLASS PRIVATE METHODS ------------//
    //////////////////////////////////////////////////

    /**
     * <p>Initialises a session with the Branch API, assigning a {@link Branch.BranchUniversalReferralInitListener}
     * to perform an action upon successful initialisation.</p>
     *
     * @param callbackContext   A callback to execute at the end of this method
     */
    private void initSession(CallbackContext callbackContext)
    {
        this.activity = this.cordova.getActivity();

        Uri data = activity.getIntent().getData();
        if (data != null && data.isHierarchical()) {
            this.deepLinkUrl = data.toString();
        }

        this.instance = Branch.getAutoInstance(this.activity.getApplicationContext());

        this.instance.initSession(new SessionListener(callbackContext), data, activity);
    }

    /**
     * <p>This method should be called if you know that a different person is about to use the app. For example,
     * if you allow users to log out and let their friend use the app, you should call this to notify Branch
     * to create a new user for this device. This will clear the first and latest params, as a new session is created.</p>
     *
     * @param callbackContext   A callback to execute at the end of this method
     */
    private void logout(CallbackContext callbackContext)
    {

        this.instance.logout(new LogoutStatusListener(callbackContext));

    }

    /**
     * <p>Redeems the specified number of credits from the "default" bucket, if there are sufficient
     * credits within it. If the number to redeem exceeds the number available in the bucket, all of
     * the available credits will be redeemed instead.</p>
     *
     * @param value An {@link Integer} specifying the number of credits to attempt to redeem from
     *              the bucket.
     * @param callbackContext   A callback to execute at the end of this method
     */
    private void redeemRewards(final int value, CallbackContext callbackContext)
    {

        this.instance.redeemRewards(value, new RedeemRewardsListener(callbackContext));

    }

    /**
     * <p>Redeems the specified number of credits from the "default" bucket, if there are sufficient
     * credits within it. If the number to redeem exceeds the number available in the bucket, all of
     * the available credits will be redeemed instead.</p>
     *
     * @param value An {@link Integer} specifying the number of credits to attempt to redeem from
     *              the bucket.
     * @param bucket The name of the bucket to remove the credits from.
     * @param callbackContext   A callback to execute at the end of this method
     */
    private void redeemRewards(int value, String bucket, CallbackContext callbackContext)
    {

        this.instance.redeemRewards(bucket, value, new RedeemRewardsListener(callbackContext));

    }

    /**
     * <p>Retrieves rewards for the current session, with a callback to perform a predefined
     * action following successful report of state change. You'll then need to call getCredits
     * in the callback to update the credit totals in your UX.</p>
     *
     * @param callbackContext   A callback to execute at the end of this method
     */
    private void loadRewards(CallbackContext callbackContext)
    {

        this.instance.loadRewards(new LoadRewardsListener(callbackContext));

    }

    /**
     * <p>Returns the parameters associated with the link that referred the session. If a user
     * clicks a link, and then opens the app, initSession will return the paramters of the link
     * and then set them in as the latest parameters to be retrieved by this method. By default,
     * sessions persist for the duration of time that the app is in focus. For example, if you
     * minimize the app, these parameters will be cleared when closeSession is called.</p>
     *
     * @param callbackContext   A callback to execute at the end of this method
     *
     * @return A {@link JSONObject} containing the latest referring parameters as
     * configured locally.
     */
    private void getLatestReferringParams(CallbackContext callbackContext)
    {

        JSONObject sessionParams = this.instance.getLatestReferringParams();

        if (sessionParams == null || sessionParams.length() == 0) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, /* send boolean: false as the data */ false));
        } else {
            Log.d(LCAT, sessionParams.toString());
            callbackContext.success(sessionParams);
        }

    }

    /**
     * <p>Returns the parameters associated with the link that referred the user. This is only set once,
     * the first time the user is referred by a link. Think of this as the user referral parameters.
     * It is also only set if isReferrable is equal to true, which by default is only true
     * on a fresh install (not upgrade or reinstall). This will change on setIdentity (if the
     * user already exists from a previous device) and logout.</p>
     *
     * @param callbackContext   A callback to execute at the end of this method
     */
    private void getFirstReferringParams(CallbackContext callbackContext)
    {

        JSONObject installParams = this.instance.getFirstReferringParams();

        if (installParams == null || installParams.length() == 0) {
            callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, /* send boolean: false as the data */ false));
        } else {
            Log.d(LCAT, installParams.toString());
            callbackContext.success(installParams);
        }

    }

    /**
     * <p>
     * Create a BranchUniversalObject with the given content.
     * </p>
     *
     * @param options A {@link JSONObject} value to set for the branch universal object properties.
     *
     * @return A {@link JSONObject} value of BranchUniversalObject instance.
     */
    private void createBranchUniversalObject(JSONObject options, CallbackContext callbackContext) throws JSONException
    {


        BranchUniversalObject branchObj = new BranchUniversalObject();

        // Set object properties
        // Facebook Properties
        if (options.has("canonicalIdentifier")) {
            branchObj.setCanonicalIdentifier(options.getString("canonicalIdentifier"));
        }
        if (options.has("title")) {
            branchObj.setTitle(options.getString("title"));
        }
        if (options.has("contentDescription")) {
            branchObj.setContentDescription(options.getString("contentDescription"));
        }
        if (options.has("contentImageUrl")) {
            branchObj.setContentImageUrl(options.getString("contentImageUrl"));
        }
        if (options.has("contentType")) {
            branchObj.setContentType(options.getString("contentType"));
        }

        // Set content visibility
        if (options.has("contentIndexingMode")) {

            if (options.getString("contentIndexingMode").equals("private")) {
                branchObj.setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PRIVATE);
            } else {
                branchObj.setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);
            }
        }

        // Add custom keys/values to the deep link data
        if (options.has("contentMetadata")) {

            JSONObject contentMetaData = options.getJSONObject("contentMetadata");
            Iterator<?> keys = contentMetaData.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = contentMetaData.optString(key);
                Log.d(LCAT, contentMetaData.getString(key));
                branchObj.addContentMetadata(key, value);
            }
        }

        BranchUniversalObjectWrapper branchObjWrapper = new BranchUniversalObjectWrapper(branchObj);

        this.branchObjectWrappers.add(branchObjWrapper);
        JSONObject response = new JSONObject();
                   response.put("message", "Success");
                   response.put("branchUniversalObjectId", this.branchObjectWrappers.size() - 1);

        callbackContext.success(response);

    }

    /**
     * Display a popup of the share sheet.
     *
     * @param instanceIdx The instance index from branchObjects array
     * @param options A {@link JSONObject} value to set URL options.
     * @param controlParams A {@link JSONObject} value to set the URL control parameters.
     */
    private void showShareSheet(int instanceIdx, JSONObject options, JSONObject controlParams, String shareText) throws JSONException
    {

        ShareSheetStyle shareSheetStyle = new ShareSheetStyle(this.activity, "Check this out!", shareText)
                .setCopyUrlStyle(this.activity.getResources().getDrawable(android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                .setMoreOptionStyle(this.activity.getResources().getDrawable(android.R.drawable.ic_menu_search), "Show More")
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                .addPreferredSharingOption(SharingHelper.SHARE_WITH.EMAIL);

        BranchUniversalObjectWrapper branchObjWrapper = (BranchUniversalObjectWrapper)this.branchObjectWrappers.get(instanceIdx);
        BranchLinkProperties linkProperties = createLinkProperties(options, controlParams);
        BranchUniversalObject branchObj = branchObjWrapper.branchUniversalObj;

        branchObj.showShareSheet(this.activity, linkProperties, shareSheetStyle,
                                new ShowShareSheetListener(branchObjWrapper.onShareLinkDialogLaunched, branchObjWrapper.onShareLinkDialogDismissed, branchObjWrapper.onLinkShareResponse, branchObjWrapper.onChannelSelected));

    }

    /**
     * Generates a share link.
     *
     * @param options A {@link JSONObject} value to set URL options.
     * @param controlParams A {@link JSONObject} value to set the URL control parameters.
     *
     * @return A {@link io.branch.referral.util.LinkProperties} value.
     */
    private BranchLinkProperties createLinkProperties(JSONObject options, JSONObject controlParams) throws JSONException
    {

        Log.d(LCAT, "Creating link properties");

        BranchLinkProperties linkProperties = new BranchLinkProperties();

        if (options.has("feature")) {
            linkProperties.setFeature(options.getString("feature"));
        }
        if (options.has("alias")) {
            linkProperties.setAlias(options.getString("alias"));
        }
        if (options.has("channel")) {
            linkProperties.setChannel(options.getString("channel"));
        }
        if (options.has("stage")) {
            linkProperties.setStage(options.getString("stage"));
        }
        if (options.has("duration")) {
            linkProperties.setDuration(options.getInt("duration"));
        }

        if (options.has("tags")) {
            ArrayList<String> tags = (ArrayList<String>) options.get("tags");

            for (String tag : tags) {
                linkProperties.addTag(tag);
            }
        }

        if (controlParams.has("$fallback_url")) {
            linkProperties.addControlParameter("$fallback_url", controlParams.getString("$fallback_url"));
        }
        if (controlParams.has("$desktop_url")) {
            linkProperties.addControlParameter("$desktop_url", controlParams.getString("$desktop_url"));
        }
        if (controlParams.has("$android_url")) {
            linkProperties.addControlParameter("$android_url", controlParams.getString("$android_url"));
        }
        if (controlParams.has("$ios_url")) {
            linkProperties.addControlParameter("$ios_url", controlParams.getString("$ios_url"));
        }
        if (controlParams.has("$ipad_url")) {
            linkProperties.addControlParameter("$ipad_url", controlParams.getString("$ipad_url"));
        }
        if (controlParams.has("$fire_url")) {
            linkProperties.addControlParameter("$fire_url", controlParams.getString("$fire_url"));
        }
        if (controlParams.has("$blackberry_url")) {
            linkProperties.addControlParameter("$blackberry_url", controlParams.getString("$blackberry_url"));
        }
        if (controlParams.has("$windows_phone_url")) {
            linkProperties.addControlParameter("$windows_phone_url", controlParams.getString("$windows_phone_url"));
        }

        return linkProperties;

    }

    /**
     * Mark the content referred by this object as viewed. This increment the view count of the contents referred by this object.
     *
     * @param instanceIdx The instance index from branchObjects array
     */
    private void registerView(int instanceIdx, CallbackContext callbackContext)
    {

        BranchUniversalObjectWrapper branchUniversalWrapper = (BranchUniversalObjectWrapper)this.branchObjectWrappers.get(instanceIdx);

        branchUniversalWrapper.branchUniversalObj.registerView(new RegisterViewStatusListener(callbackContext));

    }

    /**
     * Generate a URL.
     *
     * @param instanceIdx The instance index from branchObjects array
     * @param options A {@link JSONObject} value to set URL options.
     * @param controlParams A {@link JSONObject} value to set the URL control parameters.
     */
    private void generateShortUrl(int instanceIdx, JSONObject options, JSONObject controlParams, CallbackContext callbackContext) throws JSONException
    {

        BranchLinkProperties linkProperties = createLinkProperties(options, controlParams);

        BranchUniversalObjectWrapper branchUniversalWrapper = (BranchUniversalObjectWrapper) this.branchObjectWrappers.get(instanceIdx);

        branchUniversalWrapper.branchUniversalObj.generateShortUrl(this.activity, linkProperties, new GenerateShortUrlListener(callbackContext));

    }

    /**
     * <p>Sets the library to function in debug mode, enabling logging of all requests.</p>
     * <p>If you want to flag debug, call this <b>before</b> initUserSession</p>
     *
     * @param isEnable A {@link Boolean} value to enable/disable debugging mode for the app.
     * @param callbackContext   A callback to execute at the end of this method
     */
    private void setDebug(boolean isEnable, CallbackContext callbackContext)
    {

        this.activity = this.cordova.getActivity();

        Branch debugInstance = Branch.getAutoInstance(this.activity.getApplicationContext());

        if (isEnable) {
            debugInstance.setDebug();
        }

        callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, /* send boolean: false as the data */ isEnable));
    }

    /**
     * <p>Identifies the current user to the Branch API by supplying a unique
     * identifier as a {@link String} value.</p>
     *
     * @param newIdentity A {@link String} value containing the unique identifier of the user.
     * @param callbackContext   A callback to execute at the end of this method
     */
    private void setIdentity(String newIdentity, CallbackContext callbackContext)
    {

        this.instance.setIdentity(newIdentity, new SetIdentityListener(callbackContext));

    }

    /**
     * <p>A void call to indicate that the user has performed a specific action and for that to be
     * reported to the Branch API.</p>
     *
     * @param action A {@link String} value to be passed as an action that the user has carried out.
     *               For example "logged in" or "registered".
     * @param callbackContext   A callback to execute at the end of this method
     */
    private void userCompletedAction(String action, CallbackContext callbackContext)
    {

        this.instance.userCompletedAction(action);
        callbackContext.success("Success");

    }

    /**
     * <p>A void call to indicate that the user has performed a specific action and for that to be
     * reported to the Branch API.</p>
     *
     * @param action    A {@link String} value to be passed as an action that the user has carried
     *                  out. For example "logged in" or "registered".
     * @param metaData  A {@link JSONObject} containing app-defined meta-data to be attached to a
     *                  user action that has just been completed.
     * @param callbackContext   A callback to execute at the end of this method
     */
    private void userCompletedAction(String action, JSONObject metaData, CallbackContext callbackContext)
    {

        this.instance.userCompletedAction(action, metaData);
        callbackContext.success("Success");

    }

    /**
     * <p>Gets the credit history of the specified bucket and triggers a callback to handle the
     * response.</p>
     *
     * @param callbackContext   A callback to execute at the end of this method
     */
    private void getCreditHistory(CallbackContext callbackContext)
    {

        this.instance.getCreditHistory(new CreditHistoryListener(callbackContext));

    }

    /**
     * @access protected
     *
     * @class BranchUniversalObjectWrapper
     */
    protected class BranchUniversalObjectWrapper
    {

        public BranchUniversalObject branchUniversalObj;
        public CallbackContext onShareLinkDialogDismissed;
        public CallbackContext onShareLinkDialogLaunched;
        public CallbackContext onLinkShareResponse;
        public CallbackContext onChannelSelected;

        /**
         * @constructor
         *
         * @param BranchUniversalObject branchUniversalObj
         */
        public BranchUniversalObjectWrapper(BranchUniversalObject branchUniversalObj) {
            this.branchUniversalObj = branchUniversalObj;
            this.onShareLinkDialogDismissed = null;
            this.onShareLinkDialogLaunched = null;
            this.onLinkShareResponse = null;
            this.onChannelSelected = null;
        }

    }

    //////////////////////////////////////////////////
    //----------- INNER CLASS LISTENERS ------------//
    //////////////////////////////////////////////////

    protected class SessionListener implements Branch.BranchReferralInitListener
    {
        private CallbackContext _callbackContext;

        public SessionListener(CallbackContext callbackContext) {
            this._callbackContext = callbackContext;
        }

        //Listener that implements BranchReferralInitListener for initSession
        @Override
        public void onInitFinished(JSONObject referringParams, BranchError error) {

            String out;

            if (error == null && referringParams != null) {

                // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                // params will be empty if no data found.
                try {
                    if (referringParams.has("+clicked_branch_link") && referringParams.getBoolean("+clicked_branch_link")) {
                        out = String.format("DeepLinkHandler(%s)", referringParams.toString());
                        webView.sendJavascript(out);
                    } else if (deepLinkUrl != null) {
                        JSONObject message = new JSONObject();
                        
                        message.put("error", "Not a Branch link!");
                        message.put("url", deepLinkUrl);
                        deepLinkUrl = null;
                    
                        out = String.format("NonBranchLinkHandler(%s)", message.toString());
                        webView.sendJavascript(out);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                
                if (this._callbackContext != null) {
                    this._callbackContext.success(referringParams);
                }

            } else {
                JSONObject message = new JSONObject();
                try {
                    message.put("error", error.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (this._callbackContext != null) {
                    this._callbackContext.error(message);
                }
            }

            

        }

    }

    protected class LogoutStatusListener implements Branch.LogoutStatusListener
    {
        private CallbackContext _callbackContext;

        // Constructor that takes in a required callbackContext object
        public LogoutStatusListener (CallbackContext callbackContext) {
            this._callbackContext = callbackContext;
        }

        /**
         * Called on finishing the the logout process
         *
         * @param loggedOut A {@link Boolean} which is set to true if logout succeeded
         * @param error     An instance of {@link BranchError} to notify any error occurred during logout.
         *                  A null value is set if logout succeeded.
         */
        @Override
        public void onLogoutFinished(boolean loggedOut, BranchError error) {
            if (error == null) {
                
                branchObjectWrappers = new ArrayList<BranchUniversalObjectWrapper>();
                
                this._callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, /* send boolean: is logged out */ loggedOut));
            
            } else {

                Log.d(LCAT, "error on logout");
                
                this._callbackContext.error(error.getMessage());
            }
        }
    }

    protected class SetIdentityListener implements Branch.BranchReferralInitListener
    {
        private CallbackContext _callbackContext;

        public SetIdentityListener (CallbackContext callbackContext) {
            this._callbackContext = callbackContext;
        }

        //Listener that implements BranchReferralInitListener for setIdentity
        @Override
        public void onInitFinished(JSONObject referringParams, BranchError error) {

            if (error == null) {

                this._callbackContext.success(referringParams);

            } else {

                String errorMessage = error.getMessage();

                Log.d(LCAT, errorMessage);

                this._callbackContext.error(errorMessage);

            }

        }


    }

    protected class RegisterViewStatusListener implements BranchUniversalObject.RegisterViewStatusListener
    {

        private CallbackContext _callbackContext;

        public RegisterViewStatusListener(CallbackContext callbackContext) {
            this._callbackContext = callbackContext;
        }

        @Override
        public void onRegisterViewFinished(boolean registered, BranchError error) {

            if (error == null) {
                this._callbackContext.success("Success");
            } else {

                String errorMessage = error.getMessage();

                Log.d(LCAT, errorMessage);

                this._callbackContext.error(errorMessage);
            }
        }
    }

    protected class LoadRewardsListener implements Branch.BranchReferralStateChangedListener
    {
        private CallbackContext _callbackContext;

        // Constructor that takes in a required callbackContext object
        public LoadRewardsListener(CallbackContext callbackContext) {
            this._callbackContext = callbackContext;
        }

        // Listener that implements BranchReferralStateChangedListener for loadRewards
        @Override
        public void onStateChanged(boolean changed, BranchError error) {
            if (error == null) {

                int credits = instance.getCredits();

                this._callbackContext.success(credits);

            } else {

                String errorMessage = error.getMessage();

                Log.d(LCAT, errorMessage);

                this._callbackContext.error(errorMessage);

            }

        }
    }

    protected class RedeemRewardsListener implements Branch.BranchReferralStateChangedListener
    {
        private CallbackContext _callbackContext;

        // Constructor that takes in a required callbackContext object
        public RedeemRewardsListener(CallbackContext callbackContext) {
            this._callbackContext = callbackContext;
        }

        // Listener that implements BranchReferralStateChangedListener for redeemRewards
        @Override
        public void onStateChanged(boolean changed, BranchError error) {

            if (error == null) {

                this._callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, /* send boolean: is changed */ changed));
            
            } else {

                String errorMessage = error.getMessage();

                Log.d(LCAT, errorMessage);

                this._callbackContext.error(errorMessage);
            }
        }
    }

    protected class GenerateShortUrlListener implements Branch.BranchLinkCreateListener
    {
        private CallbackContext _callbackContext;

        // Constructor that takes in a required callbackContext object
        public GenerateShortUrlListener(CallbackContext callbackContext) {

            this._callbackContext = callbackContext;

        }

        @Override
        public void onLinkCreate(String url, BranchError error) {

            JSONObject response = new JSONObject();

            if (error == null) {

                try {
                    response.put("url", url);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                Log.d(LCAT, response.toString());
                this._callbackContext.success(response);

            } else {

                String errorMessage = error.getMessage();

                Log.d(LCAT, errorMessage);

                try {
                    response.put("error", errorMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                Log.d(LCAT, response.toString());
                this._callbackContext.error(response);

            }

        }

    }

    protected class ShowShareSheetListener implements Branch.BranchLinkShareListener
    {

        private CallbackContext _onShareLinkDialogLaunched;
        private CallbackContext _onShareLinkDialogDismissed;
        private CallbackContext _onLinkShareResponse;
        private CallbackContext _onChannelSelected;

        /**
         * @constructor
         *
         * @param CallbackContext onShareLinkDialogLaunched
         * @param CallbackContext onShareLinkDialogDismissed
         * @param CallbackContext onLinkShareResponse
         * @param CallbackContext onChannelSelected
         * */
        public ShowShareSheetListener(CallbackContext onShareLinkDialogLaunched, CallbackContext onShareLinkDialogDismissed, CallbackContext onLinkShareResponse, CallbackContext onChannelSelected) {

            this._onShareLinkDialogDismissed = onShareLinkDialogDismissed;
            this._onShareLinkDialogLaunched = onShareLinkDialogLaunched;
            this._onLinkShareResponse = onLinkShareResponse;
            this._onChannelSelected = onChannelSelected;

        }

        @Override
        public void onShareLinkDialogLaunched() {

            if (_onShareLinkDialogLaunched == null) {
                return;
            }

            PluginResult result = new PluginResult(PluginResult.Status.OK);

            result.setKeepCallback(true);

            this._onShareLinkDialogLaunched.sendPluginResult(result);

        }

        @Override
        public void onShareLinkDialogDismissed() {

            if (_onShareLinkDialogDismissed == null) {
                return;
            }

            PluginResult result = new PluginResult(PluginResult.Status.OK);

            result.setKeepCallback(true);

            this._onShareLinkDialogDismissed.sendPluginResult(result);

        }

        @Override
        public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {

            if (_onLinkShareResponse == null) {
                return;
            }

            JSONObject response = new JSONObject();

            if (error == null) {

                try {
                    response.put("sharedLink", sharedLink);
                    response.put("sharedChannel", sharedChannel);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                Log.d(LCAT, "sharedLink: " + sharedLink);
                Log.d(LCAT, "sharedChannel: " + sharedChannel);
            } else {
                String errorMessage = error.getMessage();

                Log.d(LCAT, errorMessage);

                try {
                    response.put("error", errorMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }

            Log.d(LCAT, response.toString());

            PluginResult result = new PluginResult(PluginResult.Status.OK, response);

            result.setKeepCallback(true);

            this._onLinkShareResponse.sendPluginResult(result);

        }

        @Override
        public void onChannelSelected(String channelName) {

            if (_onChannelSelected == null) {
                return;
            }

            JSONObject response = new JSONObject();

            try {
                response.put("channelName", channelName);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            Log.d(LCAT, response.toString());

            PluginResult result = new PluginResult(PluginResult.Status.OK, response);

            result.setKeepCallback(true);

            this._onChannelSelected.sendPluginResult(result);

        }
    }

    protected class CreditHistoryListener implements Branch.BranchListResponseListener
    {
        private CallbackContext _callbackContext;

        // Constructor that takes in a required callbackContext object
        public CreditHistoryListener(CallbackContext callbackContext) {
            this._callbackContext = callbackContext;
        }

        // Listener that implements BranchListResponseListener for getCreditHistory()
        @Override
        public void onReceivingResponse(JSONArray list, BranchError error) {

            ArrayList<String> errors = new ArrayList<String>();

            if (error == null) {

                JSONArray data = new JSONArray();

                if (list != null) {

                    for (int i = 0, limit = list.length(); i < limit; ++i) {

                        JSONObject entry;

                        try {
                            entry = list.getJSONObject(i);
                            data.put(entry);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            errors.add(e.getMessage());
                        }

                    }

                }

                if (errors.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (String s : errors) {
                        sb.append(s);
                        sb.append("\n");
                    }
                    this._callbackContext.error(sb.toString());
                } else {
                    this._callbackContext.success(data);
                }
            } else {

                String errorMessage = error.getMessage();

                Log.d(LCAT, errorMessage);

                this._callbackContext.error(errorMessage);

            }
        }
    }

    protected class RunnableThread implements Runnable {

        private String action;
        private JSONArray args;
        private CallbackContext callbackContext;

        public RunnableThread(String action, JSONArray args, CallbackContext callbackContext) {
            this.callbackContext = callbackContext;
            this.action = action;
            this.args = args;
        }

        public void run() {
            try {
                if (this.action.equals("setDebug")) {
                    setDebug(this.args.getBoolean(0), this.callbackContext);
                } else if (this.action.equals("initSession")) {
                    initSession(this.callbackContext);
                } else {
                    if (this.action.equals("setIdentity")) {
                        setIdentity(this.args.getString(0), this.callbackContext);
                    } else if (this.action.equals("userCompletedAction")) {
                        if (this.args.length() == 2) {
                            userCompletedAction(this.args.getString(0), this.args.getJSONObject(1), this.callbackContext);
                        } else if (this.args.length() == 1) {
                            userCompletedAction(this.args.getString(0), this.callbackContext);
                        }
                    } else if (this.action.equals("getFirstReferringParams")) {
                        getFirstReferringParams(this.callbackContext);
                    } else if (this.action.equals("getLatestReferringParams")) {
                        getLatestReferringParams(this.callbackContext);
                    } else if (this.action.equals("logout")) {
                        logout(this.callbackContext);
                    } else if (this.action.equals("loadRewards")) {
                        loadRewards(this.callbackContext);
                    } else if (this.action.equals("redeemRewards")) {
                        if (this.args.length() == 1) {
                            redeemRewards(this.args.getInt(0), this.callbackContext);
                        } else if (this.args.length() == 2) {
                            redeemRewards(this.args.getInt(0), this.args.getString(1), this.callbackContext);
                        }
                    } else if (this.action.equals("getCreditHistory")) {
                        getCreditHistory(this.callbackContext);
                    } else if (this.action.equals("createBranchUniversalObject")) {
                        createBranchUniversalObject(this.args.getJSONObject(0), this.callbackContext);
                    } else if (this.action.equals(("generateShortUrl"))) {
                        generateShortUrl(this.args.getInt(0), this.args.getJSONObject(1), this.args.getJSONObject(2), this.callbackContext);
                    } else if (this.action.equals("registerView")) {
                        registerView(this.args.getInt(0), this.callbackContext);
                    } else if (this.action.equals("showShareSheet")) {
                        showShareSheet(this.args.getInt(0), this.args.getJSONObject(1), this.args.getJSONObject(2), this.args.getString(3) != null ? this.args.getString(3) : "This stuff is awesome: ");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
