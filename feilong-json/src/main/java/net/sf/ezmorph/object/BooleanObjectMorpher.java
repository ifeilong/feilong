/*
 * Copyright 2006-2007 the original author or authors.
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

package net.sf.ezmorph.object;

import net.sf.ezmorph.MorphException;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Morphs to a Boolean.
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public final class BooleanObjectMorpher extends AbstractObjectMorpher
{
   private Boolean defaultValue;

   public BooleanObjectMorpher()
   {
      super();
   }

   /**
    * @param defaultValue return value if the value to be morphed is null
    */
   public BooleanObjectMorpher( Boolean defaultValue )
   {
      super( true );
      this.defaultValue = defaultValue;
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

      if( !(obj instanceof BooleanObjectMorpher) ){
         return false;
      }

      BooleanObjectMorpher other = (BooleanObjectMorpher) obj;
      EqualsBuilder builder = new EqualsBuilder();
      if( isUseDefault() && other.isUseDefault() ){
         builder.append( getDefaultValue(), other.getDefaultValue() );
         return builder.isEquals();
      }else if( !isUseDefault() && !other.isUseDefault() ){
         return builder.isEquals();
      }else{
         return false;
      }
   }

   /**
    * Returns the default value for this Morpher.
    */
   public Boolean getDefaultValue()
   {
      return defaultValue;
   }

   @Override
public int hashCode()
   {
      HashCodeBuilder builder = new HashCodeBuilder();
      if( isUseDefault() ){
         builder.append( getDefaultValue() );
      }
      return builder.toHashCode();
   }

   @Override
public Object morph( Object value )
   {
      if( value == null ){
         if( isUseDefault() ){
            return defaultValue;
         }else{
            throw new MorphException( "value is null" );
         }
      }

      if( value instanceof Boolean ){
         return (Boolean) value;
      }else{
         String s = String.valueOf( value );

         if( s.equalsIgnoreCase( "true" ) || s.equalsIgnoreCase( "yes" )
               || s.equalsIgnoreCase( "on" ) ){
            return Boolean.TRUE;
         }else if( s.equalsIgnoreCase( "false" ) || s.equalsIgnoreCase( "no" )
               || s.equalsIgnoreCase( "off" ) ){
            return Boolean.FALSE;
         }else if( isUseDefault() ){
            return defaultValue;
         }
      }

      throw new MorphException( "Can't morph value: " + value );
   }

   @Override
public Class morphsTo()
   {
      return Boolean.class;
   }
}