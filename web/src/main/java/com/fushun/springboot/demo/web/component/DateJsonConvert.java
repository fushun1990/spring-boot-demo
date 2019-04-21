package com.fushun.springboot.demo.web.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fushun.springboot.demo.web.data.Model1;
import com.fushun.springboot.demo.web.data.ModelChild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonComponent
public class DateJsonConvert {
    private static Logger logger = LoggerFactory.getLogger(DateJsonConvert.class);

    /**
     * 单独解析指定对象，
     * 但如果类属性还需要使用 公共的序列化，则需要每一个属性，单独调用。或者类型属性的类型已有实序列化实现，一需要单独调用
     */
    public static class Model1Serializer extends JsonSerializer<Model1> {
        @Override
        public void serialize(Model1 date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeStartObject();
            logger.info("param date:[{}]", date);
            jsonGenerator.writeObjectField(
                    "date",
                    date.getDate());
            jsonGenerator.writeObjectField(
                    "modelChild",
                    date.getModelChild());
            jsonGenerator.writeEndObject();
            System.out.println("");
        }
    }

    /**
     * 序列化 属性为Date类型的的属性值
     */
    public static class Serializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
//            jsonGenerator.writeStartObject();
            logger.info("param date:[{}]", date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            jsonGenerator.writeObject(simpleDateFormat.format(date));
//            jsonGenerator.writeStringField(
//                    "favoriteColor",
//                    getColorAsWebColor(user.getFavoriteColor()));
//            jsonGenerator.writeEndObject();
        }
    }

    /**
     * 单独对一个入参对象类型进行处理，不使用默认 httpMessageConverter
     * 可以在实现子类型，分别处理
     */
    public static class Model1DeSerializer extends JsonDeserializer<Model1> {

        @Override
        public Model1 deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
            TextNode textNode
                    = (TextNode) treeNode.get("date");
            try {
                logger.info(textNode.asText());
                Model1 model1 = new Model1();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                model1.setDate(simpleDateFormat.parse(textNode.asText()));
                ModelChild modelChild = new ModelChild();
                TreeNode modelChildTreeNode = (TreeNode) treeNode.get("modelChild");
                textNode = (TextNode) modelChildTreeNode.get("date");
                modelChild.setDate(simpleDateFormat.parse(textNode.asText()));
                model1.setModelChild(modelChild);
                return model1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}

