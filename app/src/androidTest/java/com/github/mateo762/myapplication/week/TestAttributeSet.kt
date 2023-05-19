import android.util.AttributeSet

class TestAttributeSet : AttributeSet {
    override fun getAttributeCount(): Int {
        return 0
    }

    override fun getAttributeName(index: Int): String {
        throw UnsupportedOperationException()
    }

    override fun getAttributeValue(namespace: String?, name: String?): String {
        throw UnsupportedOperationException()
    }

    override fun getAttributeValue(index: Int): String {
        throw UnsupportedOperationException()
    }

    override fun getAttributeNamespace(index: Int): String {
        throw UnsupportedOperationException()
    }

    override fun getAttributeBooleanValue(namespace: String?, attribute: String?, defaultValue: Boolean): Boolean {
        throw UnsupportedOperationException()
    }

    override fun getAttributeBooleanValue(index: Int, defaultValue: Boolean): Boolean {
        throw UnsupportedOperationException()
    }

    override fun getPositionDescription(): String {
        return "None"
    }

    override fun getAttributeNameResource(index: Int): Int {
        return 0
    }

    override fun getAttributeListValue(namespace: String?, attribute: String?, options: Array<String?>?, defaultValue: Int): Int {
        return 0
    }

    override fun getAttributeListValue(index: Int, options: Array<String?>?, defaultValue: Int): Int {
        return 0
    }

    override fun getAttributeResourceValue(namespace: String?, attribute: String?, defaultValue: Int): Int {
        return 0
    }

    override fun getAttributeResourceValue(index: Int, defaultValue: Int): Int {
        return 0
    }

    override fun getAttributeIntValue(namespace: String?, attribute: String?, defaultValue: Int): Int {
        return 0
    }

    override fun getAttributeIntValue(index: Int, defaultValue: Int): Int {
        return 0
    }

    override fun getAttributeUnsignedIntValue(namespace: String?, attribute: String?, defaultValue: Int): Int {
        return 0
    }

    override fun getAttributeUnsignedIntValue(index: Int, defaultValue: Int): Int {
        return 0
    }

    override fun getAttributeFloatValue(namespace: String?, attribute: String?, defaultValue: Float): Float {
        return 0f
    }

    override fun getAttributeFloatValue(index: Int, defaultValue: Float): Float {
        return 0f
    }

    override fun getIdAttribute(): String? {
        return "None"
    }

    override fun getClassAttribute(): String? {
        return ""
    }

    override fun getIdAttributeResourceValue(defaultValue: Int): Int {
        return 0
    }

    override fun getStyleAttribute(): Int {
        return 0
    }
    // Implement other methods based on your needs
}
