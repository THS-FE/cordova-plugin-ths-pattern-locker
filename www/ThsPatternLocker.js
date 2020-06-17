var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'ThsPatternLocker', 'coolMethod', [arg0]);
};

/**
 * 设置手势密码
 */
exports.setPwd = function(success, error){
    exec(success, error, 'ThsPatternLocker', 'setPwd', []);
};

/**
 * 验证手势密码
 */
exports.veryPwd = function(success, error){
    exec(success, error, 'ThsPatternLocker', 'veryPwd', []);
};

/**
 * 关闭验证手势密码
 */
exports.closeActivity = function(success, error){
    exec(success, error, 'ThsPatternLocker', 'closeActivity', []);
};
// 监听验证手势密码回调
exports.onVeryPwdInAndroidCallback = function(data) {
   data = JSON.stringify(data);
   data = JSON.parse(data);
   cordova.fireDocumentEvent('thsPatternLocker.onVeryPwdReceiver', data);
};

// 监听设置手势密码回调
exports.onSetPwdInAndroidCallback = function(data) {
   data = JSON.stringify(data);
   data = JSON.parse(data);
   cordova.fireDocumentEvent('thsPatternLocker.onSetPwdReceiver', data);
};
