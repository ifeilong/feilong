/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.feilong.lib.ezmorph;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.feilong.lib.ezmorph.object.IdentityObjectMorpher;

/**
 * Convenient class that manages Morphers.<br>
 * A MorpherRehistry manages a group of Morphers. A Morpher will always be
 * associated with a target class, it is possible to have several Morphers
 * registered for a target class, if this is the case, the first Morpher will be
 * used when performing a conversion and no specific Morpher is selected in
 * advance.<br>
 * {@link MorphUtils} may be used to register standard Morphers for primitive
 * types and primitive wrappers, as well as arrays of those types.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class MorpherRegistry implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3894767123320768419L;

    /** The morphers. */
    private final Map         morphers         = new HashMap();

    //---------------------------------------------------------------

    /**
     * Instantiates a new morpher registry.
     */
    public MorpherRegistry(){

    }

    /**
     * Deregisters all morphers.
     */
    public synchronized void clear(){
        morphers.clear();
    }

    //---------------------------------------------------------------

    /**
     * Returns a morpher for <code>clazz</code>.<br>
     * If several morphers are found for that class, it returns the first. If no
     * Morpher is found it will return the IdentityObjectMorpher.
     *
     * @param clazz
     *            the target class for which a Morpher may be associated
     * @return the morpher for
     */
    public synchronized Morpher getMorpherFor(Class clazz){
        List registered = (List) morphers.get(clazz);
        if (registered == null || registered.isEmpty()){
            // no morpher registered for clazz
            return IdentityObjectMorpher.getInstance();
        }
        return (Morpher) registered.get(0);
    }

    /**
     * Returns all morphers for <code>clazz</code>.<br>
     * If no Morphers are found it will return an array containing the
     * IdentityObjectMorpher.
     *
     * @param clazz
     *            the target class for which a Morpher or Morphers may be
     *            associated
     * @return the morphers for
     */
    public synchronized Morpher[] getMorphersFor(Class clazz){
        List registered = (List) morphers.get(clazz);
        if (registered == null || registered.isEmpty()){
            // no morphers registered for clazz
            return new Morpher[] { IdentityObjectMorpher.getInstance() };
        }

        //---------------------------------------------------------------
        Morpher[] morphs = new Morpher[registered.size()];
        int k = 0;
        for (Iterator i = registered.iterator(); i.hasNext();){
            morphs[k++] = (Morpher) i.next();
        }
        return morphs;
    }

    /**
     * Morphs and object to the specified target class.<br>
     * This method uses reflection to invoke primitive Morphers and Morphers that
     * do not implement ObjectMorpher.
     *
     * @param target
     *            the target class to morph to
     * @param value
     *            the value to morph
     * @return an instance of the target class if a suitable Morpher was found
     * @throws MorphException
     *             if an error occurs during the conversion
     */
    public Object morph(Class target,Object value){
        if (value == null){
            // give the first morpher in the list a shot to convert the value as we can't access type information on it
            Morpher morpher = getMorpherFor(target);
            if (morpher instanceof ObjectMorpher){
                return ((ObjectMorpher) morpher).morph(value);
            }
            try{
                Method morphMethod = morpher.getClass().getDeclaredMethod("morph", new Class[] { Object.class });
                return morphMethod.invoke(morpher, new Object[] { value });
            }catch (Exception e){
                throw new MorphException(e);
            }
        }

        //---------------------------------------------------------------
        Morpher[] morphers = getMorphersFor(target);
        for (int i = 0; i < morphers.length; i++){
            Morpher morpher = morphers[i];
            if (morpher.supports(value.getClass())){
                if (morpher instanceof ObjectMorpher){
                    return ((ObjectMorpher) morpher).morph(value);
                }

                //---------------------------------------------------------------
                try{
                    Method morphMethod = morpher.getClass().getDeclaredMethod("morph", new Class[] { Object.class });
                    return morphMethod.invoke(morpher, new Object[] { value });
                }catch (Exception e){
                    throw new MorphException(e);
                }
            }
        }
        return value;
    }

    //---------------------------------------------------------------

    /**
     * Register a Morpher for a target <code>Class</code>.<br>
     * The target class is the class this Morpher morphs to. If there are another
     * morphers registered to that class, it will be appended to a List.
     *
     * @param morpher
     *            a Morpher to register. The method <code>morphsTo()</code>
     *            is used to associate the Morpher to a target Class
     */
    public void registerMorpher(Morpher morpher){
        registerMorpher(morpher, false);
    }

    /**
     * Register a Morpher for a target <code>Class</code>.<br>
     * The target class is the class this Morpher morphs to. If there are another
     * morphers registered to that class, it will be appended to a List.
     *
     * @param morpher
     *            a Morpher to register. The method <code>morphsTo()</code>
     *            is used to associate the Morpher to a target Class
     * @param override
     *            if registering teh Morpher should override all previously
     *            registered morphers for the target type
     */
    public synchronized void registerMorpher(Morpher morpher,boolean override){
        List registered = (List) morphers.get(morpher.morphsTo());
        if (override || registered == null){
            registered = new ArrayList();
            morphers.put(morpher.morphsTo(), registered);
        }
        if (!registered.contains(morpher)){
            registered.add(morpher);
        }
    }
}