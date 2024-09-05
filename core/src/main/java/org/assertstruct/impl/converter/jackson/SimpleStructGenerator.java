package org.assertstruct.impl.converter.jackson;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.JsonEOFException;
import com.fasterxml.jackson.core.json.JsonWriteContext;
import com.fasterxml.jackson.core.util.JacksonFeatureSet;
import com.fasterxml.jackson.databind.util.RawValue;
import org.assertstruct.converter.JsonStruct;
import org.assertstruct.converter.ListWrapper;
import org.assertstruct.converter.MapWrapper;
import org.assertstruct.converter.ValueWrapper;
import org.assertstruct.service.exceptions.MatchingFailure;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;

public class SimpleStructGenerator extends JsonGenerator {
    protected final static int DEFAULT_GENERATOR_FEATURES = JsonGenerator.Feature.collectDefaults();

    /*
    /**********************************************************
    /* Configuration
    /**********************************************************
     */

    /**
     * Object codec to use for stream-based object
     * conversion through parser/generator interfaces. If null,
     * such methods cannot be used.
     */
    protected ObjectCodec _objectCodec;

    /**
     * Parse context from "parent" parser (one from which content to buffer is read,
     * if specified). Used, if available, when reading content, to present full
     * context as if content was read from the original parser: this is useful
     * in error reporting and sometimes processing as well.
     */
    protected JsonStreamContext _parentContext;

    /**
     * Bit flag composed of bits that indicate which
     * {@link com.fasterxml.jackson.core.JsonGenerator.Feature}s
     * are enabled.
     * <p>
     * NOTE: most features have no effect on this class
     */
    protected int _generatorFeatures;

    /**
     * @since 2.15
     */
    protected StreamReadConstraints _streamReadConstraints = StreamReadConstraints.defaults();

    protected boolean _closed;

    /**
     * @since 2.3
     */
    protected boolean _hasNativeTypeIds;

    /**
     * @since 2.3
     */
    protected boolean _hasNativeObjectIds;

    /**
     * @since 2.3
     */
    protected boolean _mayHaveNativeIds;

    /**
     * Flag set during construction, if use of {@link BigDecimal} is to be forced
     * on all floating-point values.
     */
    protected boolean _forceBigDecimal;

    /**
     * Generator state
     */
    RootStruct root = new RootStruct();
    JsonStruct currentStruct = root;
    Deque<JsonStruct> resultStack = new ArrayDeque<>();


    /**
     * Offset within last segment,
     */
    protected int _appendAt;

    /**
     * If native type ids supported, this is the id for following
     * value (or first token of one) to be written.
     */
    protected Object _typeId;

    /**
     * If native object ids supported, this is the id for following
     * value (or first token of one) to be written.
     */
    protected Object _objectId;

    /**
     * Do we currently have a native type or object id buffered?
     */
    protected boolean _hasNativeId = false;

    /*
    /**********************************************************
    /* Output state
    /**********************************************************
     */

    protected JsonWriteContext _writeContext;

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */

    /**
     * @param codec        Object codec to use for stream-based object
     *                     conversion through parser/generator interfaces. If null,
     *                     such methods cannot be used.
     * @param hasNativeIds Whether resulting {@link JsonParser} (if created)
     *                     is considered to support native type and object ids
     */
    public SimpleStructGenerator(ObjectCodec codec, boolean hasNativeIds) {
        _objectCodec = codec;
        _generatorFeatures = DEFAULT_GENERATOR_FEATURES;
        _writeContext = JsonWriteContext.createRootContext(null);
        _appendAt = 0;
        _hasNativeTypeIds = hasNativeIds;
        _hasNativeObjectIds = hasNativeIds;

        _mayHaveNativeIds = _hasNativeTypeIds || _hasNativeObjectIds;
    }

    public Object getRootObject() {
        return root.getValue();
    }

    public SimpleStructGenerator forceUseOfBigDecimal(boolean b) {
        _forceBigDecimal = b;
        return this;
    }

    @Override
    public Version version() {
        return com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION;
    }


    /*
    /**********************************************************
    /* JsonGenerator implementation: configuration
    /**********************************************************
     */

    @Override
    public JsonGenerator enable(Feature f) {
        _generatorFeatures |= f.getMask();
        return this;
    }

    @Override
    public JsonGenerator disable(Feature f) {
        _generatorFeatures &= ~f.getMask();
        return this;
    }

    //public JsonGenerator configure(SerializationFeature f, boolean state) { }

    @Override
    public boolean isEnabled(Feature f) {
        return (_generatorFeatures & f.getMask()) != 0;
    }

    @Override
    public int getFeatureMask() {
        return _generatorFeatures;
    }

    // Note: cannot be removed until deprecated method removed from base class
    @Override
    @Deprecated
    public JsonGenerator setFeatureMask(int mask) {
        _generatorFeatures = mask;
        return this;
    }

    @Override
    public JsonGenerator overrideStdFeatures(int values, int mask) {
        int oldState = getFeatureMask();
        _generatorFeatures = (oldState & ~mask) | (values & mask);
        return this;
    }

    @Override
    public JsonGenerator useDefaultPrettyPrinter() {
        // No-op: we don't indent
        return this;
    }

    @Override
    public JsonGenerator setCodec(ObjectCodec oc) {
        _objectCodec = oc;
        return this;
    }

    @Override
    public ObjectCodec getCodec() {
        return _objectCodec;
    }

    @Override
    public final JsonWriteContext getOutputContext() {
        return _writeContext;
    }

    /*
    /**********************************************************
    /* JsonGenerator implementation: capability introspection
    /**********************************************************
     */

    /**
     * Since we can efficiently store <code>byte[]</code>, yes.
     */
    @Override
    public boolean canWriteBinaryNatively() {
        return true; //TODO ???? Think about it
    }

    // 20-May-2020, tatu: This may or may not be enough -- ideally access is
    //    via `DeserializationContext`, not parser, but if latter is needed
    //    then we'll need to pass this from parser contents if which were
    //    buffered.
    @Override
    public JacksonFeatureSet<StreamWriteCapability> getWriteCapabilities() {
        return DEFAULT_WRITE_CAPABILITIES;
    }

    /*
    /**********************************************************
    /* JsonGenerator implementation: low-level output handling
    /**********************************************************
     */

    @Override
    public void flush() throws IOException { /* NOP */ }

    @Override
    public void close() throws IOException {
        _closed = true;
    }

    @Override
    public boolean isClosed() {
        return _closed;
    }

    /*
    /**********************************************************
    /* JsonGenerator implementation: write methods, structural
    /**********************************************************
     */

    @Override
    public final void writeStartArray() throws IOException {
        _writeContext.writeValue();
        _appendStartMarker(JsonToken.START_ARRAY);
        _writeContext = _writeContext.createChildArrayContext();
    }

    @Override // since 2.10.1
    public void writeStartArray(Object forValue) throws IOException {
        _writeContext.writeValue();
        _appendStartMarker(JsonToken.START_ARRAY);
        _writeContext = _writeContext.createChildArrayContext(forValue);
    }

    @Override // since 2.10.1
    public void writeStartArray(Object forValue, int size) throws IOException {
        _writeContext.writeValue();
        _appendStartMarker(JsonToken.START_ARRAY);
        _writeContext = _writeContext.createChildArrayContext(forValue);
    }

    @Override
    public final void writeEndArray() throws IOException {
        _appendEndMarker(JsonToken.END_ARRAY);
        // Let's allow unbalanced tho... i.e. not run out of root level, ever
        JsonWriteContext c = _writeContext.getParent();
        if (c != null) {
            _writeContext = c;
        }
    }

    @Override
    public final void writeStartObject() throws IOException {
        _writeContext.writeValue();
        _appendStartMarker(JsonToken.START_OBJECT);
        _writeContext = _writeContext.createChildObjectContext();
    }

    @Override // since 2.8
    public void writeStartObject(Object forValue) throws IOException {
        _writeContext.writeValue();
        _appendStartMarker(JsonToken.START_OBJECT);
        _writeContext = _writeContext.createChildObjectContext(forValue);
    }

    @Override // since 2.10.1
    public void writeStartObject(Object forValue, int size) throws IOException {
        _writeContext.writeValue();
        _appendStartMarker(JsonToken.START_OBJECT);
        _writeContext = _writeContext.createChildObjectContext(forValue);
    }

    @Override
    public final void writeEndObject() throws IOException {
        _appendEndMarker(JsonToken.END_OBJECT);
        // Let's allow unbalanced tho... i.e. not run out of root level, ever
        JsonWriteContext c = _writeContext.getParent();
        if (c != null) {
            _writeContext = c;
        }
    }

    @Override
    public final void writeFieldName(String name) throws IOException {
        _writeContext.writeFieldName(name);
        _appendFieldName(name);
    }

    @Override
    public void writeFieldName(SerializableString name) throws IOException {
        _writeContext.writeFieldName(name.getValue());
        _appendFieldName(name);
    }

    /*
    /**********************************************************
    /* JsonGenerator implementation: write methods, textual
    /**********************************************************
     */

    @Override
    public void writeString(String text) throws IOException {
        if (text == null) {
            writeNull();
        } else {
            _appendValue(JsonToken.VALUE_STRING, text);
        }
    }

    @Override
    public void writeString(char[] text, int offset, int len) throws IOException {
        writeString(new String(text, offset, len));
    }

    @Override
    public void writeString(SerializableString text) throws IOException {
        if (text == null) {
            writeNull();
        } else {
            _appendValue(JsonToken.VALUE_STRING, text);
        }
    }

    // @since 2.15
    @Override
    public void writeString(Reader reader, final int len) throws IOException {
        if (reader == null) {
            _reportError("null reader");
        }
        int toRead = (len >= 0) ? len : Integer.MAX_VALUE;

        // 11-Mar-2023, tatu: Really crude implementation, but it is not
        //    expected this method gets often used. Feel free to send a PR
        //    for more optimal handling if you got an itch. :)
        final char[] buf = new char[1000];
        StringBuilder sb = new StringBuilder(1000);
        while (toRead > 0) {
            int toReadNow = Math.min(toRead, buf.length);
            int numRead = reader.read(buf, 0, toReadNow);
            if (numRead <= 0) {
                break;
            }
            sb.append(buf, 0, numRead);
            toRead -= numRead;
        }
        if (toRead > 0 && len >= 0) {
            _reportError("Was not able to read enough from reader");
        }
        _appendValue(JsonToken.VALUE_STRING, sb.toString());
    }

    @Override
    public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
        // could add support for buffering if we really want it...
        _reportUnsupportedOperation();
    }

    @Override
    public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
        // could add support for buffering if we really want it...
        _reportUnsupportedOperation();
    }

    @Override
    public void writeRaw(String text) throws IOException {
        _reportUnsupportedOperation();
    }

    @Override
    public void writeRaw(String text, int offset, int len) throws IOException {
        _reportUnsupportedOperation();
    }

    @Override
    public void writeRaw(SerializableString text) throws IOException {
        _reportUnsupportedOperation();
    }

    @Override
    public void writeRaw(char[] text, int offset, int len) throws IOException {
        _reportUnsupportedOperation();
    }

    @Override
    public void writeRaw(char c) throws IOException {
        _reportUnsupportedOperation();
    }

    @Override
    public void writeRawValue(String text) throws IOException {
        _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, new RawValue(text));
    }

    @Override
    public void writeRawValue(String text, int offset, int len) throws IOException {
        if (offset > 0 || len != text.length()) {
            text = text.substring(offset, offset + len);
        }
        _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, new RawValue(text));
    }

    @Override
    public void writeRawValue(char[] text, int offset, int len) throws IOException {
        _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, new String(text, offset, len));
    }

    /*
    /**********************************************************
    /* JsonGenerator implementation: write methods, primitive types
    /**********************************************************
     */

    @Override
    public void writeNumber(short i) throws IOException {
        _appendValue(JsonToken.VALUE_NUMBER_INT, Short.valueOf(i));
    }

    @Override
    public void writeNumber(int i) throws IOException {
        _appendValue(JsonToken.VALUE_NUMBER_INT, Integer.valueOf(i));
    }

    @Override
    public void writeNumber(long l) throws IOException {
        _appendValue(JsonToken.VALUE_NUMBER_INT, Long.valueOf(l));
    }

    @Override
    public void writeNumber(double d) throws IOException {
        _appendValue(JsonToken.VALUE_NUMBER_FLOAT, Double.valueOf(d));
    }

    @Override
    public void writeNumber(float f) throws IOException {
        _appendValue(JsonToken.VALUE_NUMBER_FLOAT, Float.valueOf(f));
    }

    @Override
    public void writeNumber(BigDecimal dec) throws IOException {
        if (dec == null) {
            writeNull();
        } else {
            _appendValue(JsonToken.VALUE_NUMBER_FLOAT, dec);
        }
    }

    @Override
    public void writeNumber(BigInteger v) throws IOException {
        if (v == null) {
            writeNull();
        } else {
            _appendValue(JsonToken.VALUE_NUMBER_INT, v);
        }
    }

    @Override
    public void writeNumber(String encodedValue) throws IOException {
        /* 03-Dec-2010, tatu: related to [JACKSON-423], should try to keep as numeric
         *   identity as long as possible
         */
        _appendValue(JsonToken.VALUE_NUMBER_FLOAT, encodedValue);
    }

    private void writeLazyInteger(Object encodedValue) throws IOException {
        _appendValue(JsonToken.VALUE_NUMBER_INT, encodedValue);
    }

    private void writeLazyDecimal(Object encodedValue) throws IOException {
        _appendValue(JsonToken.VALUE_NUMBER_FLOAT, encodedValue);
    }

    @Override
    public void writeBoolean(boolean state) throws IOException {
        _appendValue(state ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE);
    }

    @Override
    public void writeNull() throws IOException {
        _appendValue(JsonToken.VALUE_NULL);
    }

    /*
    /***********************************************************
    /* JsonGenerator implementation: write methods for POJOs/trees
    /***********************************************************
     */

    @Override
    public void writeObject(Object value) throws IOException {
        if (value == null) {
            writeNull();
            return;
        }
        Class<?> raw = value.getClass();
        if (raw == byte[].class || (value instanceof RawValue)) {
            _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, value);
            return;
        }
        if (_objectCodec == null) {
            // 28-May-2014, tatu: Tricky choice here; if no codec, should we
            //   err out, or just embed? For now, do latter.

//          throw new XxxException("No ObjectCodec configured for SimpleStructGenerator, writeObject() called");
            _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, value);
        } else {
            _objectCodec.writeValue(this, value);
        }
    }

    @Override
    public void writeTree(TreeNode node) throws IOException {
        if (node == null) {
            writeNull();
            return;
        }

        if (_objectCodec == null) {
            // as with 'writeObject()', is codec optional?
            _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, node);
        } else {
            _objectCodec.writeTree(this, node);
        }
    }

    /*
    /***********************************************************
    /* JsonGenerator implementation; binary
    /***********************************************************
     */

    @Override
    public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {
        /* 31-Dec-2009, tatu: can do this using multiple alternatives; but for
         *   now, let's try to limit number of conversions.
         *   The only (?) tricky thing is that of whether to preserve variant,
         *   seems pointless, so let's not worry about it unless there's some
         *   compelling reason to.
         */
        byte[] copy = new byte[len];
        System.arraycopy(data, offset, copy, 0, len);
        writeObject(copy);
    }

    /**
     * Although we could support this method, it does not necessarily make
     * sense: we cannot make good use of streaming because buffer must
     * hold all the data. Because of this, currently this will simply
     * throw {@link UnsupportedOperationException}
     */
    @Override
    public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) {
        throw new UnsupportedOperationException();
    }

    /*
    /***********************************************************
    /* JsonGenerator implementation: native ids
    /***********************************************************
     */

    @Override
    public boolean canWriteTypeId() {
        return _hasNativeTypeIds;
    }

    @Override
    public boolean canWriteObjectId() {
        return _hasNativeObjectIds;
    }

    @Override
    public void writeTypeId(Object id) {
        _typeId = id;
        _hasNativeId = true;
    }

    @Override
    public void writeObjectId(Object id) {
        _objectId = id;
        _hasNativeId = true;
    }

    @Override // since 2.8
    public void writeEmbeddedObject(Object object) throws IOException {
        _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, object);
    }

    /*
    /**********************************************************
    /* JsonGenerator implementation; pass-through copy
    /**********************************************************
     */

    @Override
    public void copyCurrentEvent(JsonParser p) throws IOException {
        if (_mayHaveNativeIds) {
            _checkNativeIds(p);
        }
        switch (p.currentToken()) {
            case START_OBJECT:
                writeStartObject();
                break;
            case END_OBJECT:
                writeEndObject();
                break;
            case START_ARRAY:
                writeStartArray();
                break;
            case END_ARRAY:
                writeEndArray();
                break;
            case FIELD_NAME:
                writeFieldName(p.currentName());
                break;
            case VALUE_STRING:
                if (p.hasTextCharacters()) {
                    writeString(p.getTextCharacters(), p.getTextOffset(), p.getTextLength());
                } else {
                    writeString(p.getText());
                }
                break;
            case VALUE_NUMBER_INT:
                switch (p.getNumberType()) {
                    case INT:
                        writeNumber(p.getIntValue());
                        break;
                    case BIG_INTEGER:
                        writeLazyInteger(p.getNumberValueDeferred());
                        break;
                    default:
                        writeNumber(p.getLongValue());
                }
                break;
            case VALUE_NUMBER_FLOAT:
                writeLazyDecimal(p.getNumberValueDeferred());
                break;
            case VALUE_TRUE:
                writeBoolean(true);
                break;
            case VALUE_FALSE:
                writeBoolean(false);
                break;
            case VALUE_NULL:
                writeNull();
                break;
            case VALUE_EMBEDDED_OBJECT:
                writeObject(p.getEmbeddedObject());
                break;
            default:
                throw new MatchingFailure("Internal error: unexpected token: " + p.currentToken());
        }
    }

    @Override
    public void copyCurrentStructure(JsonParser p) throws IOException {
        JsonToken t = p.currentToken();

        // Let's handle field-name separately first
        if (t == JsonToken.FIELD_NAME) {
            if (_mayHaveNativeIds) {
                _checkNativeIds(p);
            }
            writeFieldName(p.currentName());
            t = p.nextToken();
            // fall-through to copy the associated value
        } else if (t == null) {
            // 13-Dec-2023, tatu: For some unexpected EOF cases we may end up here, so:
            throw new JsonEOFException(p, null, "Unexpected end-of-input");
        }

        // We'll do minor handling here to separate structured, scalar values,
        // then delegate appropriately.
        // Plus also deal with oddity of "dangling" END_OBJECT/END_ARRAY
        switch (t) {
            case START_ARRAY:
                if (_mayHaveNativeIds) {
                    _checkNativeIds(p);
                }
                writeStartArray();
                _copyBufferContents(p);
                break;
            case START_OBJECT:
                if (_mayHaveNativeIds) {
                    _checkNativeIds(p);
                }
                writeStartObject();
                _copyBufferContents(p);
                break;
            case END_ARRAY:
                writeEndArray();
                break;
            case END_OBJECT:
                writeEndObject();
                break;
            default: // others are simple:
                _copyBufferValue(p, t);
        }
    }

    protected void _copyBufferContents(JsonParser p) throws IOException {
        int depth = 1;
        JsonToken t;

        while ((t = p.nextToken()) != null) {
            switch (t) {
                case FIELD_NAME:
                    if (_mayHaveNativeIds) {
                        _checkNativeIds(p);
                    }
                    writeFieldName(p.currentName());
                    break;

                case START_ARRAY:
                    if (_mayHaveNativeIds) {
                        _checkNativeIds(p);
                    }
                    writeStartArray();
                    ++depth;
                    break;

                case START_OBJECT:
                    if (_mayHaveNativeIds) {
                        _checkNativeIds(p);
                    }
                    writeStartObject();
                    ++depth;
                    break;

                case END_ARRAY:
                    writeEndArray();
                    if (--depth == 0) {
                        return;
                    }
                    break;
                case END_OBJECT:
                    writeEndObject();
                    if (--depth == 0) {
                        return;
                    }
                    break;

                default:
                    _copyBufferValue(p, t);
            }
        }
    }

    // NOTE: Copied from earlier `copyCurrentEvent()`
    private void _copyBufferValue(JsonParser p, JsonToken t) throws IOException {
        if (_mayHaveNativeIds) {
            _checkNativeIds(p);
        }
        switch (t) {
            case VALUE_STRING:
                if (p.hasTextCharacters()) {
                    writeString(p.getTextCharacters(), p.getTextOffset(), p.getTextLength());
                } else {
                    writeString(p.getText());
                }
                break;
            case VALUE_NUMBER_INT:
                switch (p.getNumberType()) {
                    case INT:
                        writeNumber(p.getIntValue());
                        break;
                    case BIG_INTEGER:
                        writeLazyInteger(p.getNumberValueDeferred());
                        break;
                    default:
                        writeNumber(p.getLongValue());
                }
                break;
            case VALUE_NUMBER_FLOAT:
                writeLazyDecimal(p.getNumberValueDeferred());
                break;
            case VALUE_TRUE:
                writeBoolean(true);
                break;
            case VALUE_FALSE:
                writeBoolean(false);
                break;
            case VALUE_NULL:
                writeNull();
                break;
            case VALUE_EMBEDDED_OBJECT:
                writeObject(p.getEmbeddedObject());
                break;
            default:
                throw new MatchingFailure("Internal error: unexpected token: " + t);
        }
    }

    private final void _checkNativeIds(JsonParser p) throws IOException {
        if ((_typeId = p.getTypeId()) != null) {
            _hasNativeId = true;
        }
        if ((_objectId = p.getObjectId()) != null) {
            _hasNativeId = true;
        }
    }

    /*
    /**********************************************************
    /* Internal methods
    /**********************************************************
     */

    /**
     * Method used for appending token known to represent a "simple" scalar
     * value where token is the only information
     */
    protected final void _appendValue(JsonToken type) {
        _writeContext.writeValue();
        Object value;
        Object currentValue = currentValue();
        if (type == JsonToken.VALUE_TRUE) {
            value = currentValue instanceof Boolean ? currentValue : new ValueWrapper(currentValue, Boolean.TRUE);
        } else if (type == JsonToken.VALUE_FALSE) {
            value = currentValue instanceof Boolean ? currentValue : new ValueWrapper(currentValue, Boolean.FALSE);
        } else if (type == JsonToken.VALUE_NULL) {
            value = currentValue == null ? null : new ValueWrapper(currentValue, null);
        } else {
            throw new MatchingFailure("Internal error: unexpected token: " + type);
        }
        currentStruct.addChild(_writeContext.getCurrentName(), value);
    }

    /**
     * Method used for appending token known to represent a scalar value
     * where there is additional content (text, number) beyond type token
     */
    protected final void _appendValue(JsonToken type, Object value) {
        _writeContext.writeValue();
        Object currentValue = currentValue();
        if (value == currentValue || (value != null && currentValue != null && value.getClass() == currentValue.getClass())) {
            currentStruct.addChild(_writeContext.getCurrentName(), value);
        } else {
            currentStruct.addChild(_writeContext.getCurrentName(), new ValueWrapper(currentValue, value));
        }
    }

    /**
     * Specialized method used for appending a field name, appending either
     * {@link String} or {@link SerializableString}.
     */
    protected final void _appendFieldName(Object value) {
        // NOP
    }

    /**
     * Specialized method used for appending a structural start Object/Array marker
     */
    protected final void _appendStartMarker(JsonToken type) {
        JsonStruct newStruct;
        if (type == JsonToken.START_OBJECT) {
            newStruct = new MapWrapper(currentValue());
        } else if (type == JsonToken.START_ARRAY) {
            newStruct = new ListWrapper(currentValue());
        } else {
            throw new MatchingFailure("Internal error: unexpected token: " + type);
        }
        currentStruct.addChild(_writeContext.getCurrentName(), newStruct);
        resultStack.push(currentStruct);
        currentStruct = newStruct;
    }

    /**
     * Specialized method used for appending a structural end Object/Array marker
     */
    protected final void _appendEndMarker(JsonToken type) {
        if (resultStack.isEmpty()) {
            throw new MatchingFailure("Internal error: unexpected token end: " + type);
        }
        currentStruct = resultStack.pop();
    }

    @Override
    protected void _reportUnsupportedOperation() {
        throw new UnsupportedOperationException("Called operation not supported for SimpleStructGenerator");
    }

    boolean skipNextValue=false;

    public void skipNextValue() {
        skipNextValue=true;
    }

    @Override
    public void assignCurrentValue(Object v) {
        if (skipNextValue) {
            skipNextValue=false;
        } else {
            super.assignCurrentValue(v);
        }
    }
}
