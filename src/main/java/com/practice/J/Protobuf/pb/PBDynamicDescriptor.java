package com.practice.J.Protobuf.pb;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;

public class PBDynamicDescriptor {

    /* sample
    {
        "result": [
            {
                "name": "Apple",
                    "quantity": 10
            },
            {
                "name": "banana",
                    "quantity": 20
            }
        ]
    }
    */

    //google protobuf : DynamicMessage API
    public static Descriptors.Descriptor getDescriptor() throws Descriptors.DescriptorValidationException {

        DescriptorProtos.DescriptorProto fruitDescriptor = DescriptorProtos.DescriptorProto.newBuilder()
                .setName("Fruit")
                .addField(DescriptorProtos.FieldDescriptorProto.newBuilder()
                        .setName("name")
                        .setNumber(1)
                        .setType(DescriptorProtos.FieldDescriptorProto.Type.TYPE_STRING))
                .addField(DescriptorProtos.FieldDescriptorProto.newBuilder()
                        .setName("quantity")
                        .setNumber(2)
                        .setType(DescriptorProtos.FieldDescriptorProto.Type.TYPE_INT32))
                .build();


        DescriptorProtos.DescriptorProto resultDescriptor = DescriptorProtos.DescriptorProto.newBuilder()
                .setName("Result")
                .addField(DescriptorProtos.FieldDescriptorProto.newBuilder()
                        .setName("result")
                        .setNumber(1)
                        .setType(DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE)
                        .setLabel(DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED)
                        .setTypeName("Fruit"))
                .build();

        Descriptors.FileDescriptor fileDescriptor = Descriptors.FileDescriptor
                .buildFrom(DescriptorProtos.FileDescriptorProto.newBuilder()
                        .addMessageType(fruitDescriptor)
                        .addMessageType(resultDescriptor)
                        .build(), new Descriptors.FileDescriptor[]{});

        return fileDescriptor.findMessageTypeByName("Result");

    }

}
