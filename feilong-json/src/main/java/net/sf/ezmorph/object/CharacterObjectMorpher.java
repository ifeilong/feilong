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
 * Morphs to a Character.
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public final class CharacterObjectMorpher extends AbstractObjectMorpher
{
   private Character defaultValue;

   public CharacterObjectMorpher()
   {
      super();
   }

   /**
    * @param defaultValue return value if the value to be morphed is null
    */
   public CharacterObjectMorpher( Character defaultValue )
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

      if( !(obj instanceof CharacterObjectMorpher) ){
         return false;
      }

      CharacterObjectMorpher other = (CharacterObjectMorpher) obj;
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
   public Character getDefaultValue()
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

      if( value instanceof Character ){
         return (Character) value;
      }else{
         String s = String.valueOf( value );
         if( s.length() > 0 ){
            return new Character( s.charAt( 0 ) );
         }else{
            if( isUseDefault() ){
               return defaultValue;
            }else{
               throw new MorphException( "Can't morph value: " + value );
            }
         }
      }
   }

   @Override
public Class morphsTo()
   {
      return Character.class;
   }
}