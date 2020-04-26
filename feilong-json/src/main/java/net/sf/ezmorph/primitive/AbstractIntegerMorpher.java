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

package net.sf.ezmorph.primitive;

import java.util.Locale;

/**
 * Base class por primitive integer conversion.
 *
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public abstract class AbstractIntegerMorpher extends AbstractPrimitiveMorpher
{
   public AbstractIntegerMorpher()
   {
      super();
   }

   /**
    * @param useDefault if morph() should return a default value if the value to
    *        be morphed is null
    */
   public AbstractIntegerMorpher( boolean useDefault )
   {
      super( useDefault );
   }

   /**
    * Trims the String from the begining to the first "."
    */
   protected String getIntegerValue( Object obj )
   {
      // use en_US Locale
      Locale defaultLocale = Locale.getDefault();
      String str = null;
      try{
         Locale.setDefault( Locale.US );
         str = String.valueOf( obj );
      }
      finally{
         Locale.setDefault( defaultLocale );
      }

      int index = str.indexOf( "." );
      if( index != -1 ){
         str = str.substring( 0, index );
      }
      return str;
   }
}