#import <AssetsLibrary/AssetsLibrary.h>
#import <Cordova/CDV.h>
#import <Cordova/CDVPlugin.h>
#import <Cordova/CDVInvokedUrlCommand.h>

#import "CameraPreview.h"

@implementation CameraPreview
//static cameraIsReady = NO;

- (void) startCamera:(CDVInvokedUrlCommand*)command {
    //        [self.commandDelegate runInBackground:^{
    //                            sleep(3);
    //
    
    CDVPluginResult *pluginResult;
    
    if (self.sessionManager != nil) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Camera already started!"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        return;
    }
    
    if (command.arguments.count > 3) {
        CGFloat x = (CGFloat)[command.arguments[0] floatValue] + self.webView.frame.origin.x;
        CGFloat y = (CGFloat)[command.arguments[1] floatValue] + self.webView.frame.origin.y;
        CGFloat width = (CGFloat)[command.arguments[2] floatValue];
        CGFloat height = (CGFloat)[command.arguments[3] floatValue];
        NSString *defaultCamera = command.arguments[4];
        BOOL tapToTakePicture = (BOOL)[command.arguments[5] boolValue];
        BOOL dragEnabled = (BOOL)[command.arguments[6] boolValue];
        BOOL toBack = (BOOL)[command.arguments[7] boolValue];
        // sleep(3);
        // Create the session manager
        self.sessionManager = [[CameraSessionManager alloc] init];
        
        //render controller setup
//        self.cameraRenderController = [[CameraRenderController alloc] init];
        self.cameraRenderController = [[CameraRenderController alloc] initWithWebView:(UIWebView*)self.webView];//  [[CameraRenderController alloc] init];
        self.cameraRenderController.dragEnabled = dragEnabled;
        self.cameraRenderController.tapToTakePicture = tapToTakePicture;
        self.cameraRenderController.sessionManager = self.sessionManager;
        self.cameraRenderController.view.frame = CGRectMake(x, y, width, height);
        self.cameraRenderController.delegate = self;
        
        [self.viewController addChildViewController:self.cameraRenderController];
        
        
        //                dispatch_async(dispatch_get_main_queue(), ^{
        //display the camera bellow the webview
        if (toBack) {
            //make transparent
            self.webView.opaque = NO;
            self.webView.backgroundColor = [UIColor clearColor];
            
            //                            self.webView.layer.zPosition = 4;
            //                            self.viewController.view.layer.zPosition = 1;
            //                            self.cameraRenderController.view.layer.zPosition = 4;
            
            [self.viewController.view insertSubview:self.cameraRenderController.view atIndex:0];
            //                        [self.viewController.view bringSubviewToFront:self.cameraRenderController.view];
            
        } else {
            self.cameraRenderController.view.alpha = (CGFloat)[command.arguments[8] floatValue];
            
            [self.viewController.view addSubview:self.cameraRenderController.view];
        }
        //       });
        // Setup session
        
        self.sessionManager.delegate = self.cameraRenderController;
        [self.sessionManager setupSession:defaultCamera];
        
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Invalid number of parameters"];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    
    //            }];
}

- (void) stopCamera:(CDVInvokedUrlCommand*)command {
    NSLog(@"stopCamera");
    CDVPluginResult *pluginResult;
    
    if(self.sessionManager != nil) {
        [self.cameraRenderController.view removeFromSuperview];
        [self.cameraRenderController removeFromParentViewController];
        self.cameraRenderController = nil;
        
        [self.sessionManager.session stopRunning];
        self.sessionManager = nil;
        
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    }
    else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Camera not started"];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
- (void) focusCamera:(CDVInvokedUrlCommand*)command {
    CDVPluginResult *pluginResult;
    if (self.sessionManager != nil) {
        //[self.sessionManager focusCamera];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Camera not started"];
    }
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    //                [[videoDeviceInput device] focusPointOfInterest];
    // TODO: Make camera focus.
    
    // if (self.cameraRenderController != nil) {
    //     [self.cameraRenderController.view setHidden:YES];
    // }
}
- (void) hideCamera:(CDVInvokedUrlCommand*)command {
    NSLog(@"hideCamera");
    CDVPluginResult *pluginResult;
    
    if (self.cameraRenderController != nil) {
        [self.cameraRenderController.view setHidden:YES];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Camera not started"];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void) showCamera:(CDVInvokedUrlCommand*)command {
    NSLog(@"showCamera");
    CDVPluginResult *pluginResult;
    
    if (self.cameraRenderController != nil) {
        [self.cameraRenderController.view setHidden:NO];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Camera not started"];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void) switchCamera:(CDVInvokedUrlCommand*)command {
    NSLog(@"switchCamera");
    CDVPluginResult *pluginResult;
    
    if (self.sessionManager != nil) {
        [self.sessionManager switchCamera];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Camera not started"];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void) takePicture:(CDVInvokedUrlCommand*)command {
    NSLog(@"takePicture");
    CDVPluginResult *pluginResult;
    
    if (self.cameraRenderController != NULL) {
        CGFloat maxW = (CGFloat)[command.arguments[0] floatValue];
        CGFloat maxH = (CGFloat)[command.arguments[1] floatValue];
        [self invokeTakePicture:maxW withHeight:maxH];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Camera not started"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
}

-(void) setOnPictureTakenHandler:(CDVInvokedUrlCommand*)command {
    NSLog(@"setOnPictureTakenHandler");
    self.onPictureTakenHandlerId = command.callbackId;
}

-(void) setColorEffect:(CDVInvokedUrlCommand*)command {
    NSLog(@"setColorEffect");
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    NSString *filterName = command.arguments[0];
    
    if ([filterName isEqual: @"none"]) {
        dispatch_async(self.sessionManager.sessionQueue, ^{
            [self.sessionManager setCiFilter:nil];
        });
    } else if ([filterName isEqual: @"mono"]) {
        dispatch_async(self.sessionManager.sessionQueue, ^{
            CIFilter *filter = [CIFilter filterWithName:@"CIColorMonochrome"];
            [filter setDefaults];
            [self.sessionManager setCiFilter:filter];
        });
    } else if ([filterName isEqual: @"negative"]) {
        dispatch_async(self.sessionManager.sessionQueue, ^{
            CIFilter *filter = [CIFilter filterWithName:@"CIColorInvert"];
            [filter setDefaults];
            [self.sessionManager setCiFilter:filter];
        });
    } else if ([filterName isEqual: @"posterize"]) {
        dispatch_async(self.sessionManager.sessionQueue, ^{
            CIFilter *filter = [CIFilter filterWithName:@"CIColorPosterize"];
            [filter setDefaults];
            [self.sessionManager setCiFilter:filter];
        });
    } else if ([filterName isEqual: @"sepia"]) {
        dispatch_async(self.sessionManager.sessionQueue, ^{
            CIFilter *filter = [CIFilter filterWithName:@"CISepiaTone"];
            [filter setDefaults];
            [self.sessionManager setCiFilter:filter];
        });
    } else if ([filterName isEqual: @"frame"]) {
        dispatch_async(self.sessionManager.sessionQueue, ^{
            CIFilter *filter = [CIFilter filterWithName:@"CIPhotoEffectProcess"]; //CIPhotoEffectTransfer? CIPhotoEffectInstant?
            [filter setDefaults];
            [self.sessionManager setCiFilter:filter];
        });
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Filter not found"];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
- (void) invokeTakePicture {
    [self invokeTakePicture:0.0 withHeight:0.0];
}
+ (NSString *) applicationDocumentsDirectoryOld
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *basePath = ([paths count] > 0) ? [paths objectAtIndex:0] : nil;
    return basePath;
}
+ (NSString *)applicationDocumentsDirectory {
    return [[[[NSFileManager defaultManager] URLsForDirectory:NSLibraryDirectory
                                                    inDomains:NSUserDomainMask] lastObject].path stringByAppendingPathComponent:@"NoCloud"];
}

+ (NSString *)saveImage:(UIImage *)image withName:(NSString *)name {
    NSData *data = UIImageJPEGRepresentation(image, 1.0);
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *fullPath = [[CameraPreview applicationDocumentsDirectory] stringByAppendingPathComponent:name];
    [fileManager createFileAtPath:fullPath contents:data attributes:nil];
    
    return fullPath;
}

+ (UIImage *)normalizedImage:(UIImage *)image {
    if (image.imageOrientation == UIImageOrientationUp) return image;
    
    UIGraphicsBeginImageContextWithOptions(image.size, NO, image.scale);
    [image drawInRect:(CGRect){0, 0, image.size}];
    UIImage *normalizedImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return normalizedImage;
}
- (double)radiansFromUIImageOrientation:(UIImageOrientation)orientation
{
    double radians;
    
    switch (orientation) {
        case UIImageOrientationUp:
        case UIImageOrientationUpMirrored:
            radians = M_PI_2;
            break;
        case UIImageOrientationLeft:
        case UIImageOrientationLeftMirrored:
            radians = 0.f;
            break;
        case UIImageOrientationRight:
        case UIImageOrientationRightMirrored:
            radians = M_PI;
            break;
        case UIImageOrientationDown:
        case UIImageOrientationDownMirrored:
            radians = -M_PI_2;
            break;
    }
    
    return radians;
}

-(CGImageRef) CGImageRotated:(CGImageRef) originalCGImage withRadiants:(double) radians
{
    CGSize imageSize = CGSizeMake(CGImageGetWidth(originalCGImage), CGImageGetHeight(originalCGImage));
    CGSize rotatedSize;
    if (radians == M_PI_2 || radians == -M_PI_2) {
        rotatedSize = CGSizeMake(imageSize.height, imageSize.width);
    } else {
        rotatedSize = imageSize;
    }
    
    double rotatedCenterX = rotatedSize.width / 2.f;
    double rotatedCenterY = rotatedSize.height / 2.f;
    
    UIGraphicsBeginImageContextWithOptions(rotatedSize, NO, 1.f);
    CGContextRef rotatedContext = UIGraphicsGetCurrentContext();
    if (radians == 0.f || radians == M_PI) { // 0 or 180 degrees
        CGContextTranslateCTM(rotatedContext, rotatedCenterX, rotatedCenterY);
        if (radians == 0.0f) {
            CGContextScaleCTM(rotatedContext, 1.f, -1.f);
        } else {
            CGContextScaleCTM(rotatedContext, -1.f, 1.f);
        }
        CGContextTranslateCTM(rotatedContext, -rotatedCenterX, -rotatedCenterY);
    } else if (radians == M_PI_2 || radians == -M_PI_2) { // +/- 90 degrees
        CGContextTranslateCTM(rotatedContext, rotatedCenterX, rotatedCenterY);
        CGContextRotateCTM(rotatedContext, radians);
        CGContextScaleCTM(rotatedContext, 1.f, -1.f);
        CGContextTranslateCTM(rotatedContext, -rotatedCenterY, -rotatedCenterX);
    }
    
    CGRect drawingRect = CGRectMake(0.f, 0.f, imageSize.width, imageSize.height);
    CGContextDrawImage(rotatedContext, drawingRect, originalCGImage);
    CGImageRef rotatedCGImage = CGBitmapContextCreateImage(rotatedContext);
    
    UIGraphicsEndImageContext();
    CFAutorelease((CFTypeRef)rotatedCGImage);
    
    return rotatedCGImage;
}

- (void) invokeTakePicture:(CGFloat) maxWidth withHeight:(CGFloat) maxHeight {
    AVCaptureConnection *connection = [self.sessionManager.stillImageOutput connectionWithMediaType:AVMediaTypeVideo];
    __weak CameraRenderController *weakRenderController = self.cameraRenderController;
    //     __weak CameraSessionManager *weakSessionManager = self.sessionManager;
    __weak CameraPreview *weakSelf = self;
    [self.sessionManager.stillImageOutput captureStillImageAsynchronouslyFromConnection:connection completionHandler:^(CMSampleBufferRef sampleBuffer, NSError *error) {
        NSLog(@"Done creating still image");
        
        if (error) {
            NSLog(@"%@", error);
        } else {
            NSData *imageData = [AVCaptureStillImageOutput jpegStillImageNSDataRepresentation:sampleBuffer];
            UIImage *capturedImage  = [CameraPreview normalizedImage:[[UIImage alloc] initWithData:imageData]];
            
            CIImage *capturedCImage;
            //image resize
            
            if(maxWidth > 0 && maxHeight > 0) {
                CGFloat scaleHeight = maxWidth/capturedImage.size.height;
                CGFloat scaleWidth = maxHeight/capturedImage.size.width;
                CGFloat scale = scaleHeight > scaleWidth ? scaleWidth : scaleHeight;
                
                CIFilter *resizeFilter = [CIFilter filterWithName:@"CILanczosScaleTransform"];
                [resizeFilter setValue:[[CIImage alloc] initWithCGImage:[capturedImage CGImage]] forKey:kCIInputImageKey];
                [resizeFilter setValue:[NSNumber numberWithFloat:1.0f] forKey:@"inputAspectRatio"];
                [resizeFilter setValue:[NSNumber numberWithFloat:scale] forKey:@"inputScale"];
                capturedCImage = [resizeFilter outputImage];
            }
            else{
                capturedCImage = [[CIImage alloc] initWithCGImage:[capturedImage CGImage]];
            }
            
            CIImage *imageToFilter;
            CIImage *finalCImage;
            
            //fix front mirroring
            if (self.sessionManager.defaultCamera == AVCaptureDevicePositionFront) {
                CGAffineTransform matrix = CGAffineTransformTranslate(CGAffineTransformMakeScale(1, -1), 0, capturedCImage.extent.size.height);
                imageToFilter = [capturedCImage imageByApplyingTransform:matrix];
            } else {
                imageToFilter = capturedCImage;
            }
            
            CIFilter *filter = [self.sessionManager ciFilter];
            if (filter != nil) {
                [self.sessionManager.filterLock lock];
                [filter setValue:imageToFilter forKey:kCIInputImageKey];
                finalCImage = [filter outputImage];
                [self.sessionManager.filterLock unlock];
            } else {
                finalCImage = imageToFilter;
            }
            CGImageRef finalImage = [weakRenderController.ciContext createCGImage:finalCImage fromRect:finalCImage.extent];;;
            __block NSString *originalPicturePath;
            NSString *fileName = [[[NSUUID UUID] UUIDString] stringByAppendingString:@".jpg"];
            // CIContext *context = [CIContext contextWithOptions:nil];
            
            UIImage *resultImage = [UIImage imageWithCGImage:finalImage];
            
            CFRelease(finalImage);
            
            double radiants = [weakSelf radiansFromUIImageOrientation:resultImage.imageOrientation];
            
            CGImageRef resultFinalImage = [weakSelf CGImageRotated:finalImage withRadiants:radiants];
            CGImageRef resultFinalImage1 = [weakSelf CGImageRotated:resultFinalImage withRadiants:radiants];
            UIImage *saveUIImage = [UIImage imageWithCGImage:resultFinalImage1];
            originalPicturePath = [CameraPreview saveImage: saveUIImage withName: fileName];
            
            NSLog(@"originalPicturePath: %@", originalPicturePath);
            dispatch_group_t group = dispatch_group_create();
            dispatch_group_notify(group, dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_HIGH, 0), ^{
                NSMutableArray *params = [[NSMutableArray alloc] init];
                
                [params addObject:originalPicturePath];
                
                CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:params];
                [pluginResult setKeepCallbackAsBool:true];
                [self.commandDelegate sendPluginResult:pluginResult callbackId:self.onPictureTakenHandlerId];
            });
        }
    }];
}
@end
