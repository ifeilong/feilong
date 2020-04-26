/*
 * Copyright 2006-2007-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.ezmorph.bean;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.MorphUtils;
import net.sf.ezmorph.MorpherRegistry;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public final class MorphDynaBean implements DynaBean, Serializable
{
   private static final long serialVersionUID = -605547389232706344L;
   private MorphDynaClass dynaClass;
   private Map dynaValues = new HashMap();
   private MorpherRegistry morpherRegistry;

   public MorphDynaBean()
   {
      this( null );
   }

   public MorphDynaBean( MorpherRegistry morpherRegistry )
   {
      setMorpherRegistry( morpherRegistry );
   }

   @Override
public boolean contains( String name, String key )
   {
      DynaProperty dynaProperty = getDynaProperty( name );

      Class type = dynaProperty.getType();
      if( !Map.class.isAssignableFrom( type ) ){
         throw new MorphException( "Non-Mapped property name: " + name + " key: " + key );
      }

      Object value = dynaValues.get( name );
      if( value == null ){
         value = new HashMap();
         dynaValues.put( name, value );
      }
      return ((Map) value).containsKey( key );
   }

   @Override
public boolean equals( Object obj )
   {
      if( this == obj ){
         return true;
      }

      if( obj == null ){
         return false;
      }

      if( !(obj instanceof MorphDynaBean) ){
         return false;
      }

      MorphDynaBean other = (MorphDynaBean) obj;
      EqualsBuilder builder = new EqualsBuilder().append( this.dynaClass, other.dynaClass );
      DynaProperty[] props = dynaClass.getDynaProperties();
      for( int i = 0; i < props.length; i++ ){
         DynaProperty prop = props[i];
         builder.append( dynaValues.get( prop.getName() ), dynaValues.get( prop.getName() ) );
      }
      return builder.isEquals();
   }

   @Override
public Object get( String name )
   {
      Object value = dynaValues.get( name );

      if( value != null ){
         return value;
      }

      Class type = getDynaProperty( name ).getType();
      if( !type.isPrimitive() ){
         return value;
      }else{
         return morpherRegistry.morph( type, value );
      }
   }

   @Override
public Object get( String name, int index )
   {
      DynaProperty dynaProperty = getDynaProperty( name );

      Class type = dynaProperty.getType();
      if( !type.isArray() && !List.class.isAssignableFrom( type ) ){
         throw new MorphException( "Non-Indexed property name: " + name + " index: " + index );
      }

      Object value = dynaValues.get( name );

      if( value.getClass()
            .isArray() ){
         value = Array.get( value, index );
      }else if( value instanceof List ){
         value = ((List) value).get( index );
      }

      return value;
   }

   @Override
public Object get( String name, String key )
   {
      DynaProperty dynaProperty = getDynaProperty( name );

      Class type = dynaProperty.getType();
      if( !Map.class.isAssignableFrom( type ) ){
         throw new MorphException( "Non-Mapped property name: " + name + " key: " + key );
      }

      Object value = dynaValues.get( name );
      if( value == null ){
         value = new HashMap();
         dynaValues.put( name, value );
      }
      return ((Map) value).get( key );
   }

   @Override
public DynaClass getDynaClass()
   {
      return this.dynaClass;
   }

   public MorpherRegistry getMorpherRegistry()
   {
      return morpherRegistry;
   }

   @Override
public int hashCode()
   {
      HashCodeBuilder builder = new HashCodeBuilder().append( dynaClass );
      DynaProperty[] props = dynaClass.getDynaProperties();
      for( int i = 0; i < props.length; i++ ){
         DynaProperty prop = props[i];
         builder.append( dynaValues.get( prop.getName() ) );
      }
      return builder.toHashCode();
   }

   @Override
public void remove( String name, String key )
   {
      DynaProperty dynaProperty = getDynaProperty( name );

      Class type = dynaProperty.getType();
      if( !Map.class.isAssignableFrom( type ) ){
         throw new MorphException( "Non-Mapped property name: " + name + " key: " + key );
      }

      Object value = dynaValues.get( name );
      if( value == null ){
         value = new HashMap();
         dynaValues.put( name, value );
      }
      ((Map) value).remove( key );
   }

   @Override
public void set( String name, int index, Object value )
   {
      DynaProperty dynaProperty = getDynaProperty( name );

      Class type = dynaProperty.getType();
      if( !type.isArray() && !List.class.isAssignableFrom( type ) ){
         throw new MorphException( "Non-Indexed property name: " + name + " index: " + index );
      }

      Object prop = dynaValues.get( name );
      if( prop == null ){
         if( List.class.isAssignableFrom( type ) ){
            prop = new ArrayList();
         }else{
            prop = Array.newInstance( type.getComponentType(), index + 1 );
         }
         dynaValues.put( name, prop );
      }

      if( prop.getClass()
            .isArray() ){
         if( index >= Array.getLength( prop ) ){
            Object tmp = Array.newInstance( type.getComponentType(), index + 1 );
            System.arraycopy( prop, 0, tmp, 0, Array.getLength( prop ) );
            prop = tmp;
            dynaValues.put( name, tmp );
         }
         Array.set( prop, index, value );
      }else if( prop instanceof List ){
         List l = (List) prop;
         if( index >= l.size() ){
            for( int i = l.size(); i <= index + 1; i++ ){
               l.add( null );
            }
         }
         ((List) prop).set( index, value );
      }
   }

   @Override
public void set( String name, Object value )
   {
      DynaProperty property = getDynaProperty( name );

      if( value == null || !isDynaAssignable( property.getType(), value.getClass() ) ){
         value = morpherRegistry.morph( property.getType(), value );
      }

      dynaValues.put( name, value );
   }

   @Override
public void set( String name, String key, Object value )
   {
      DynaProperty dynaProperty = getDynaProperty( name );

      Class type = dynaProperty.getType();
      if( !Map.class.isAssignableFrom( type ) ){
         throw new MorphException( "Non-Mapped property name: " + name + " key: " + key );
      }

      Object prop = dynaValues.get( name );
      if( prop == null ){
         prop = new HashMap();
         dynaValues.put( name, prop );
      }
      ((Map) prop).put( key, value );
   }

   public synchronized void setDynaBeanClass( MorphDynaClass dynaClass )
   {
      if( this.dynaClass == null ){
         this.dynaClass = dynaClass;
      }
   }

   public void setMorpherRegistry( MorpherRegistry morpherRegistry )
   {
      if( morpherRegistry == null ){
         this.morpherRegistry = new MorpherRegistry();
         MorphUtils.registerStandardMorphers( this.morpherRegistry );
      }else{
         this.morpherRegistry = morpherRegistry;
      }
   }

   @Override
public String toString()
   {
      return new ToStringBuilder( this, ToStringStyle.MULTI_LINE_STYLE ).append( dynaValues )
            .toString();
   }

   protected DynaProperty getDynaProperty( String name )
   {
      DynaProperty property = getDynaClass().getDynaProperty( name );
      if( property == null ){
         throw new MorphException( "Unspecified property for " + name );
      }
      return property;
   }

   protected boolean isDynaAssignable( Class dest, Class src )
   {
      boolean assignable = dest.isAssignableFrom( src );
      if( assignable ){
         return true;
      }
      assignable = (dest == Boolean.TYPE && src == Boolean.class) ? true : assignable;
      assignable = (dest == Byte.TYPE && src == Byte.class) ? true : assignable;
      assignable = (dest == Character.TYPE && src == Character.class) ? true : assignable;
      assignable = (dest == Short.TYPE && src == Short.class) ? true : assignable;
      assignable = (dest == Integer.TYPE && src == Integer.class) ? true : assignable;
      assignable = (dest == Long.TYPE && src == Long.class) ? true : assignable;
      assignable = (dest == Float.TYPE && src == Float.class) ? true : assignable;
      assignable = (dest == Double.TYPE && src == Double.class) ? true : assignable;

      if( src == Double.TYPE || Double.class.isAssignableFrom( src ) ){
         assignable = (isByte( dest ) || isShort( dest ) || isInteger( dest ) || isLong( dest ) || isFloat( dest )) ? true
               : assignable;
      }
      if( src == Float.TYPE || Float.class.isAssignableFrom( src ) ){
         assignable = (isByte( dest ) || isShort( dest ) || isInteger( dest ) || isLong( dest )) ? true
               : assignable;
      }
      if( src == Long.TYPE || Long.class.isAssignableFrom( src ) ){
         assignable = (isByte( dest ) || isShort( dest ) || isInteger( dest )) ? true : assignable;
      }
      if( src == Integer.TYPE || Integer.class.isAssignableFrom( src ) ){
         assignable = (isByte( dest ) || isShort( dest )) ? true : assignable;
      }
      if( src == Short.TYPE || Short.class.isAssignableFrom( src ) ){
         assignable = (isByte( dest )) ? true : assignable;
      }

      return assignable;
   }

   private boolean isByte( Class clazz )
   {
      return Byte.class.isAssignableFrom( clazz ) || clazz == Byte.TYPE;
   }

   private boolean isFloat( Class clazz )
   {
      return Float.class.isAssignableFrom( clazz ) || clazz == Float.TYPE;
   }

   private boolean isInteger( Class clazz )
   {
      return Integer.class.isAssignableFrom( clazz ) || clazz == Integer.TYPE;
   }

   private boolean isLong( Class clazz )
   {
      return Long.class.isAssignableFrom( clazz ) || clazz == Long.TYPE;
   }

   private boolean isShort( Class clazz )
   {
      return Short.class.isAssignableFrom( clazz ) || clazz == Short.TYPE;
   }
}