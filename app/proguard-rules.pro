# Release builds are not minified, so no rules are required.
# Kept for safety if minification is turned on later:
-keep class com.secondspace.app.AdminReceiver { *; }
-keep class com.secondspace.app.GetProvisioningModeActivity { *; }
-keep class com.secondspace.app.PolicyComplianceActivity { *; }
