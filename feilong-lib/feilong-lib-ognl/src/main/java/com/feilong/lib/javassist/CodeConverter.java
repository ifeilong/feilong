/*
 * Javassist, a Java-bytecode translator toolkit.
 * Copyright (C) 1999- Shigeru Chiba. All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License.  Alternatively, the contents of this file may be used under
 * the terms of the GNU Lesser General Public License Version 2.1 or later,
 * or the Apache License Version 2.0.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 */

package com.feilong.lib.javassist;

import com.feilong.lib.javassist.bytecode.BadBytecode;
import com.feilong.lib.javassist.bytecode.CodeAttribute;
import com.feilong.lib.javassist.bytecode.CodeIterator;
import com.feilong.lib.javassist.bytecode.ConstPool;
import com.feilong.lib.javassist.bytecode.MethodInfo;
import com.feilong.lib.javassist.convert.TransformReadField;
import com.feilong.lib.javassist.convert.TransformWriteField;
import com.feilong.lib.javassist.convert.Transformer;

/**
 * Simple translator of method bodies
 * (also see the <code>javassist.expr</code> package).
 *
 * <p>
 * Instances of this class specifies how to instrument of the
 * bytecodes representing a method body. They are passed to
 * <code>CtClass.instrument()</code> or
 * <code>CtMethod.instrument()</code> as a parameter.
 *
 * <p>
 * Example:
 * 
 * <pre>
 * ClassPool cp = ClassPool.getDefault();
 * CtClass point = cp.get("Point");
 * CtClass singleton = cp.get("Singleton");
 * CtClass client = cp.get("Client");
 * CodeConverter conv = new CodeConverter();
 * conv.replaceNew(point, singleton, "makePoint");
 * client.instrument(conv);
 * </pre>
 *
 * <p>
 * This program substitutes "<code>Singleton.makePoint()</code>"
 * for all occurrences of "<code>new Point()</code>"
 * appearing in methods declared in a <code>Client</code> class.
 *
 * @see com.feilong.lib.javassist.CtClass#instrument(CodeConverter)
 * @see com.feilong.lib.javassist.CtMethod#instrument(CodeConverter)
 * @see com.feilong.lib.javassist.expr.ExprEditor
 */
public class CodeConverter{

    protected Transformer transformers = null;

    /**
     * Modify a method body so that an expression reading the specified
     * field is replaced with a call to the specified <i>static</i> method.
     * This static method receives the target object of the original
     * read expression as a parameter. It must return a value of
     * the same type as the field.
     *
     * <p>
     * For example, the program below
     *
     * <pre>
     * 
     * Point p = new Point();
     * 
     * int newX = p.x + 3;
     * </pre>
     *
     * <p>
     * can be translated into:
     *
     * <pre>
     * 
     * Point p = new Point();
     * 
     * int newX = Accessor.readX(p) + 3;
     * </pre>
     *
     * <p>
     * where
     *
     * <pre>
     * public class Accessor {
     *     public static int readX(Object target) { ... }
     * }
     * </pre>
     *
     * <p>
     * The type of the parameter of <code>readX()</code> must
     * be <code>java.lang.Object</code> independently of the actual
     * type of <code>target</code>. The return type must be the same
     * as the field type.
     *
     * @param field
     *            the field.
     * @param calledClass
     *            the class in which the static method is
     *            declared.
     * @param calledMethod
     *            the name of the static method.
     */
    public void replaceFieldRead(CtField field,CtClass calledClass,String calledMethod){
        transformers = new TransformReadField(transformers, field, calledClass.getName(), calledMethod);
    }

    /**
     * Modify a method body so that an expression writing the specified
     * field is replaced with a call to the specified static method.
     * This static method receives two parameters: the target object of
     * the original
     * write expression and the assigned value. The return type of the
     * static method is <code>void</code>.
     *
     * <p>
     * For example, the program below
     *
     * <pre>
     * Point p = new Point();
     * p.x = 3;
     * </pre>
     *
     * <p>
     * can be translated into:
     *
     * <pre>
     * Point p = new Point();
     * Accessor.writeX(3);
     * </pre>
     *
     * <p>
     * where
     *
     * <pre>
     * public class Accessor {
     *     public static void writeX(Object target, int value) { ... }
     * }
     * </pre>
     *
     * <p>
     * The type of the first parameter of <code>writeX()</code> must
     * be <code>java.lang.Object</code> independently of the actual
     * type of <code>target</code>. The type of the second parameter
     * is the same as the field type.
     *
     * @param field
     *            the field.
     * @param calledClass
     *            the class in which the static method is
     *            declared.
     * @param calledMethod
     *            the name of the static method.
     */
    public void replaceFieldWrite(CtField field,CtClass calledClass,String calledMethod){
        transformers = new TransformWriteField(transformers, field, calledClass.getName(), calledMethod);
    }

    /**
     * Performs code conversion.
     */
    protected void doit(CtClass clazz,MethodInfo minfo,ConstPool cp) throws CannotCompileException{
        Transformer t;
        CodeAttribute codeAttr = minfo.getCodeAttribute();
        if (codeAttr == null || transformers == null){
            return;
        }
        for (t = transformers; t != null; t = t.getNext()){
            t.initialize(cp, clazz, minfo);
        }

        CodeIterator iterator = codeAttr.iterator();
        while (iterator.hasNext()){
            try{
                int pos = iterator.next();
                for (t = transformers; t != null; t = t.getNext()){
                    pos = t.transform(clazz, pos, iterator, cp);
                }
            }catch (BadBytecode e){
                throw new CannotCompileException(e);
            }
        }

        int locals = 0;
        int stack = 0;
        for (t = transformers; t != null; t = t.getNext()){
            int s = t.extraLocals();
            if (s > locals){
                locals = s;
            }

            s = t.extraStack();
            if (s > stack){
                stack = s;
            }
        }

        for (t = transformers; t != null; t = t.getNext()){
            t.clean();
        }

        if (locals > 0){
            codeAttr.setMaxLocals(codeAttr.getMaxLocals() + locals);
        }

        if (stack > 0){
            codeAttr.setMaxStack(codeAttr.getMaxStack() + stack);
        }

        try{
            minfo.rebuildStackMapIf6(clazz.getClassPool(), clazz.getClassFile2());
        }catch (BadBytecode b){
            throw new CannotCompileException(b.getMessage(), b);
        }
    }

    /**
     * Interface containing the method names to be used
     * as array access replacements.
     *
     * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
     * @version $Revision: 1.16 $
     */
    public interface ArrayAccessReplacementMethodNames{

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;I)B</code> to replace reading from a byte[].
         */
        String byteOrBooleanRead();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;IB)V</code> to replace writing to a byte[].
         */
        String byteOrBooleanWrite();

        /**
         * @return the name of a static method with the signature
         *         <code>(Ljava/lang/Object;I)C</code> to replace reading from a char[].
         */
        String charRead();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;IC)V</code> to replace writing to a byte[].
         */
        String charWrite();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;I)D</code> to replace reading from a double[].
         */
        String doubleRead();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;ID)V</code> to replace writing to a double[].
         */
        String doubleWrite();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;I)F</code> to replace reading from a float[].
         */
        String floatRead();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;IF)V</code> to replace writing to a float[].
         */
        String floatWrite();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;I)I</code> to replace reading from a int[].
         */
        String intRead();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;II)V</code> to replace writing to a int[].
         */
        String intWrite();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;I)J</code> to replace reading from a long[].
         */
        String longRead();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;IJ)V</code> to replace writing to a long[].
         */
        String longWrite();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;I)Ljava/lang/Object;</code>
         * to replace reading from a Object[] (or any subclass of object).
         */
        String objectRead();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;ILjava/lang/Object;)V</code>
         * to replace writing to a Object[] (or any subclass of object).
         */
        String objectWrite();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;I)S</code> to replace reading from a short[].
         */
        String shortRead();

        /**
         * Returns the name of a static method with the signature
         * <code>(Ljava/lang/Object;IS)V</code> to replace writing to a short[].
         */
        String shortWrite();
    }

}
