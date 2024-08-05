package ua.kiev.its.assertstruct.impl.converter.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import ua.kiev.its.assertstruct.config.JsonConverterI;

public class JacksonConverter implements JsonConverterI {
    ObjectMapper mapper;

    public JacksonConverter() {
        mapper = new ObjectMapper();
 /*       mapper.registerModule(new Module() {
            @Override
            public String getModuleName() {
                return "JSONIFY";
            }

            @Override
            public Version version() {
                return new Version(1, 0, 0, null, null, null);
            }

            @Override
            public void setupModule(SetupContext context) {
                context.addBeanSerializerModifier(new BeanSerializerModifier() {

                });
            }
        });*/
    }
    public Object pojo2json(Object value) {
        return mapper.convertValue(value, Object.class);
    }
/*
    public Object pojo2json(Object value) {
         mapper.writeValue(new JsonGenerator() {
             @Override
             public JsonGenerator setCodec(ObjectCodec oc) {
                 return null;
             }

             @Override
             public ObjectCodec getCodec() {
                 return null;
             }

             @Override
             public Version version() {
                 return null;
             }

             @Override
             public JsonStreamContext getOutputContext() {
                 return null;
             }

             @Override
             public JsonGenerator enable(Feature f) {
                 return null;
             }

             @Override
             public JsonGenerator disable(Feature f) {
                 return null;
             }

             @Override
             public boolean isEnabled(Feature f) {
                 return false;
             }

             @Override
             public int getFeatureMask() {
                 return 0;
             }

             @Override
             public JsonGenerator setFeatureMask(int values) {
                 return null;
             }

             @Override
             public JsonGenerator useDefaultPrettyPrinter() {
                 return null;
             }

             @Override
             public void writeStartArray() throws IOException {

             }

             @Override
             public void writeEndArray() throws IOException {

             }

             @Override
             public void writeStartObject() throws IOException {

             }

             @Override
             public void writeEndObject() throws IOException {

             }

             @Override
             public void writeFieldName(String name) throws IOException {

             }

             @Override
             public void writeFieldName(SerializableString name) throws IOException {

             }

             @Override
             public void writeString(String text) throws IOException {

             }

             @Override
             public void writeString(char[] buffer, int offset, int len) throws IOException {

             }

             @Override
             public void writeString(SerializableString text) throws IOException {

             }

             @Override
             public void writeRawUTF8String(byte[] buffer, int offset, int len) throws IOException {

             }

             @Override
             public void writeUTF8String(byte[] buffer, int offset, int len) throws IOException {

             }

             @Override
             public void writeRaw(String text) throws IOException {

             }

             @Override
             public void writeRaw(String text, int offset, int len) throws IOException {

             }

             @Override
             public void writeRaw(char[] text, int offset, int len) throws IOException {

             }

             @Override
             public void writeRaw(char c) throws IOException {

             }

             @Override
             public void writeRawValue(String text) throws IOException {

             }

             @Override
             public void writeRawValue(String text, int offset, int len) throws IOException {

             }

             @Override
             public void writeRawValue(char[] text, int offset, int len) throws IOException {

             }

             @Override
             public void writeBinary(Base64Variant bv, byte[] data, int offset, int len) throws IOException {

             }

             @Override
             public int writeBinary(Base64Variant bv, InputStream data, int dataLength) throws IOException {
                 return 0;
             }

             @Override
             public void writeNumber(int v) throws IOException {

             }

             @Override
             public void writeNumber(long v) throws IOException {

             }

             @Override
             public void writeNumber(BigInteger v) throws IOException {

             }

             @Override
             public void writeNumber(double v) throws IOException {

             }

             @Override
             public void writeNumber(float v) throws IOException {

             }

             @Override
             public void writeNumber(BigDecimal v) throws IOException {

             }

             @Override
             public void writeNumber(String encodedValue) throws IOException {

             }

             @Override
             public void writeBoolean(boolean state) throws IOException {

             }

             @Override
             public void writeNull() throws IOException {

             }

             @Override
             public void writeObject(Object pojo) throws IOException {

             }

             @Override
             public void writeTree(TreeNode rootNode) throws IOException {

             }

             @Override
             public void flush() throws IOException {

             }

             @Override
             public boolean isClosed() {
                 return false;
             }

             @Override
             public void close() throws IOException {

             }
         });
    }
*/
}
