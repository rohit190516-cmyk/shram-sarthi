package com.example.shramsarthi.data.firebase

/**
 * Actionable exceptions for user profile operations in Firestore.
 * Provides clear, categorized messages in Hindi and English.
 */
sealed class ProfileException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    class PermissionDenied(cause: Throwable? = null) : ProfileException(
        "सुरक्षा नियम अनुमति अस्वीकृत (Permission Denied): कृपया सुनिश्चित करें कि आप अपने अधिकृत खाते से जुड़े हैं।",
        cause
    )

    class Unauthenticated(cause: Throwable? = null) : ProfileException(
        "सत्र समाप्त (Session Expired / Unauthenticated): आपका लॉगिन सत्र अमान्य है। कृपया पुनः ओटीपी से लॉगिन करें।",
        cause
    )

    class NetworkUnavailable(cause: Throwable? = null) : ProfileException(
        "इंटरनेट या सर्वर अनुपलब्ध (Network Unavailable): सर्वर से संपर्क नहीं हो सका। कृपया इंटरनेट कनेक्शन की जांच करें।",
        cause
    )

    class Timeout(cause: Throwable? = null) : ProfileException(
        "अनुरोध समय समाप्त (Request Timed Out): सर्वर प्रतिक्रिया में अधिक समय लग रहा है। पुनः प्रयास करने पर प्रोफ़ाइल स्थिति जांची जाएगी।",
        cause
    )

    class QuotaExceeded(cause: Throwable? = null) : ProfileException(
        "सर्वर सीमा पार (Quota Exceeded): अत्यधिक अनुरोध। कृपया कुछ समय बाद पुनः प्रयास करें।",
        cause
    )

    class InvalidData(val reason: String, cause: Throwable? = null) : ProfileException(
        "अमान्य विवरण (Invalid Profile Data): $reason",
        cause
    )

    class OperationFailed(val details: String, cause: Throwable? = null) : ProfileException(
        "प्रोफ़ाइल सहेजने में विफल: $details",
        cause
    )
}
