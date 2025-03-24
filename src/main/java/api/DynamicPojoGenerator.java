package api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/*
 * Used to generate the pojo class for the given input json body 
 * 
 * @author Selva 
 */
public class DynamicPojoGenerator {

	public static void generatePojoFromJson(String json, String outputDirectory, String className, String packageName)
			throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(json);
		TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC);
		processJsonNode(rootNode, classBuilder);
		JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).build();
		File outputDir = new File(outputDirectory);
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}
		javaFile.writeTo(outputDir);
	}

	private static void processJsonNode(JsonNode node, TypeSpec.Builder classBuilder) {
		if (node.isObject()) {
			Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
			while (fields.hasNext()) {
				Map.Entry<String, JsonNode> field = fields.next();
				String fieldName = field.getKey();
				JsonNode fieldValue = field.getValue();
				FieldSpec.Builder fieldSpecBuilder = FieldSpec.builder(String.class, fieldName, Modifier.PRIVATE);

				if (fieldValue.isObject()) {
					String nestedClassName = capitalize(fieldName);
					TypeSpec.Builder nestedClassBuilder = TypeSpec.classBuilder(nestedClassName)
							.addModifiers(Modifier.PUBLIC);
					processJsonNode(fieldValue, nestedClassBuilder);
					classBuilder.addType(nestedClassBuilder.build());
					fieldSpecBuilder = FieldSpec.builder(ClassName.get("", nestedClassName), fieldName,
							Modifier.PRIVATE);
				} else if (fieldValue.isArray()) {
					String arrayType = fieldValue.get(0).getNodeType().toString();
				} else {
					if (fieldValue.isTextual()) {
						fieldSpecBuilder = FieldSpec.builder(String.class, fieldName, Modifier.PRIVATE);
					} else if (fieldValue.isInt()) {
						fieldSpecBuilder = FieldSpec.builder(int.class, fieldName, Modifier.PRIVATE);
					} else if (fieldValue.isBoolean()) {
						fieldSpecBuilder = FieldSpec.builder(boolean.class, fieldName, Modifier.PRIVATE);
					}
				}
				classBuilder.addField(fieldSpecBuilder.build());
				generateGetterSetter(fieldName, fieldSpecBuilder.build().type, classBuilder);
			}
		}
	}

	private static void generateGetterSetter(String fieldName, TypeName fieldType, TypeSpec.Builder classBuilder) {
		MethodSpec getter = MethodSpec.methodBuilder("get" + capitalize(fieldName)).addModifiers(Modifier.PUBLIC)
				.returns(fieldType).addStatement("return " + fieldName).build();
		MethodSpec setter = MethodSpec.methodBuilder("set" + capitalize(fieldName)).addModifiers(Modifier.PUBLIC)
				.addParameter(fieldType, fieldName).addStatement("this." + fieldName + " = " + fieldName).build();
		classBuilder.addMethod(getter);
		classBuilder.addMethod(setter);
	}

	private static String capitalize(String str) {
		if (str == null || str.isEmpty()) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static void main(String[] args) throws IOException {
		String json = "";
		String outputDirectory = "src/main/java";
		generatePojoFromJson(json, outputDirectory, "PojoClassData", "com.pojoclass.data");
	}
}
