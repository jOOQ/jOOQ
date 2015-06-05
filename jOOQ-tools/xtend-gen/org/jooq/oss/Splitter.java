/**
 * Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)
 * All rights reserved.
 * 
 * This work is dual-licensed
 * - under the Apache Software License 2.0 (the "ASL")
 * - under the jOOQ License and Maintenance Agreement (the "jOOQ License")
 * =============================================================================
 * You may choose which license applies to you:
 * 
 * - If you're using this work with Open Source databases, you may choose
 *   either ASL or jOOQ License.
 * - If you're using this work with at least one commercial database, you must
 *   choose jOOQ License
 * 
 * For more information, please visit http://www.jooq.org/licenses
 * 
 * Apache Software License 2.0:
 * -----------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * jOOQ License and Maintenance Agreement:
 * -----------------------------------------------------------------------------
 * Data Geekery grants the Customer the non-exclusive, timely limited and
 * non-transferable license to install and use the Software under the terms of
 * the jOOQ License and Maintenance Agreement.
 * 
 * This library is distributed with a LIMITED WARRANTY. See the jOOQ License
 * and Maintenance Agreement for more details: http://www.jooq.org/licensing
 */
package org.jooq.oss;

import com.google.common.base.Objects;
import java.io.File;
import java.io.FileFilter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.jooq.SQLDialect;
import org.jooq.xtend.Generators;

@SuppressWarnings("all")
public class Splitter extends Generators {
  private static ExecutorService ex;
  
  private static AtomicInteger charsTotal = new AtomicInteger(0);
  
  private static AtomicInteger charsMasked = new AtomicInteger(0);
  
  private List<String> tokens;
  
  public static void split(final String workspace, final String... tokens) {
    try {
      ExecutorService _newFixedThreadPool = Executors.newFixedThreadPool(4);
      Splitter.ex = _newFixedThreadPool;
      final Splitter splitter = new Splitter(tokens);
      File _file = new File("../..");
      final File workspaceIn = _file.getCanonicalFile();
      String _canonicalPath = workspaceIn.getCanonicalPath();
      String _plus = (_canonicalPath + workspace);
      File _file_1 = new File(_plus);
      final File workspaceOut = _file_1.getCanonicalFile();
      final FileFilter _function = (File it) -> {
        String _name = it.getName();
        return _name.startsWith("jOOQ");
      };
      File[] _listFiles = workspaceIn.listFiles(_function);
      for (final File project : _listFiles) {
        {
          String _name = project.getName();
          final File in = new File(workspaceIn, _name);
          String _name_1 = project.getName();
          final File out = new File(workspaceOut, _name_1);
          splitter.transform(in, out, in);
        }
      }
      Splitter.ex.shutdown();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void transform(final File inRoot, final File outRoot, final File in) {
    try {
      String _canonicalPath = outRoot.getCanonicalPath();
      String _plus = (_canonicalPath + "/");
      String _canonicalPath_1 = in.getCanonicalPath();
      String _canonicalPath_2 = inRoot.getCanonicalPath();
      String _replace = _canonicalPath_1.replace(_canonicalPath_2, "");
      String _plus_1 = (_plus + _replace);
      final File out = new File(_plus_1);
      boolean _isDirectory = in.isDirectory();
      if (_isDirectory) {
        final FileFilter _function = (File it) -> {
          try {
            boolean _and = false;
            boolean _and_1 = false;
            boolean _and_2 = false;
            boolean _and_3 = false;
            boolean _and_4 = false;
            boolean _and_5 = false;
            boolean _and_6 = false;
            boolean _and_7 = false;
            boolean _and_8 = false;
            boolean _and_9 = false;
            boolean _and_10 = false;
            boolean _and_11 = false;
            boolean _and_12 = false;
            boolean _and_13 = false;
            boolean _and_14 = false;
            boolean _and_15 = false;
            boolean _and_16 = false;
            boolean _and_17 = false;
            boolean _and_18 = false;
            boolean _and_19 = false;
            boolean _and_20 = false;
            boolean _and_21 = false;
            String _canonicalPath_3 = it.getCanonicalPath();
            boolean _endsWith = _canonicalPath_3.endsWith(".class");
            boolean _not = (!_endsWith);
            if (!_not) {
              _and_21 = false;
            } else {
              String _canonicalPath_4 = it.getCanonicalPath();
              boolean _endsWith_1 = _canonicalPath_4.endsWith(".dat");
              boolean _not_1 = (!_endsWith_1);
              _and_21 = _not_1;
            }
            if (!_and_21) {
              _and_20 = false;
            } else {
              String _canonicalPath_5 = it.getCanonicalPath();
              boolean _endsWith_2 = _canonicalPath_5.endsWith(".git");
              boolean _not_2 = (!_endsWith_2);
              _and_20 = _not_2;
            }
            if (!_and_20) {
              _and_19 = false;
            } else {
              String _canonicalPath_6 = it.getCanonicalPath();
              boolean _endsWith_3 = _canonicalPath_6.endsWith(".jar");
              boolean _not_3 = (!_endsWith_3);
              _and_19 = _not_3;
            }
            if (!_and_19) {
              _and_18 = false;
            } else {
              String _canonicalPath_7 = it.getCanonicalPath();
              boolean _endsWith_4 = _canonicalPath_7.endsWith(".pdf");
              boolean _not_4 = (!_endsWith_4);
              _and_18 = _not_4;
            }
            if (!_and_18) {
              _and_17 = false;
            } else {
              String _canonicalPath_8 = it.getCanonicalPath();
              boolean _endsWith_5 = _canonicalPath_8.endsWith(".zip");
              boolean _not_5 = (!_endsWith_5);
              _and_17 = _not_5;
            }
            if (!_and_17) {
              _and_16 = false;
            } else {
              String _canonicalPath_9 = it.getCanonicalPath();
              boolean _endsWith_6 = _canonicalPath_9.endsWith("._trace");
              boolean _not_6 = (!_endsWith_6);
              _and_16 = _not_6;
            }
            if (!_and_16) {
              _and_15 = false;
            } else {
              String _canonicalPath_10 = it.getCanonicalPath();
              boolean _contains = _canonicalPath_10.contains("jOOQ-explorer");
              boolean _not_7 = (!_contains);
              _and_15 = _not_7;
            }
            if (!_and_15) {
              _and_14 = false;
            } else {
              String _canonicalPath_11 = it.getCanonicalPath();
              boolean _contains_1 = _canonicalPath_11.contains("jOOQ-test");
              boolean _not_8 = (!_contains_1);
              _and_14 = _not_8;
            }
            if (!_and_14) {
              _and_13 = false;
            } else {
              String _canonicalPath_12 = it.getCanonicalPath();
              boolean _contains_2 = _canonicalPath_12.contains("jOOQ-tools");
              boolean _not_9 = (!_contains_2);
              _and_13 = _not_9;
            }
            if (!_and_13) {
              _and_12 = false;
            } else {
              String _canonicalPath_13 = it.getCanonicalPath();
              boolean _contains_3 = _canonicalPath_13.contains("jOOQ-vaadin-example");
              boolean _not_10 = (!_contains_3);
              _and_12 = _not_10;
            }
            if (!_and_12) {
              _and_11 = false;
            } else {
              String _canonicalPath_14 = it.getCanonicalPath();
              boolean _contains_4 = _canonicalPath_14.contains("jOOQ-website");
              boolean _not_11 = (!_contains_4);
              _and_11 = _not_11;
            }
            if (!_and_11) {
              _and_10 = false;
            } else {
              String _canonicalPath_15 = it.getCanonicalPath();
              boolean _contains_5 = _canonicalPath_15.contains("jOOQ-websites");
              boolean _not_12 = (!_contains_5);
              _and_10 = _not_12;
            }
            if (!_and_10) {
              _and_9 = false;
            } else {
              String _canonicalPath_16 = it.getCanonicalPath();
              boolean _contains_6 = _canonicalPath_16.contains("jOOQ-webservices");
              boolean _not_13 = (!_contains_6);
              _and_9 = _not_13;
            }
            if (!_and_9) {
              _and_8 = false;
            } else {
              String _canonicalPath_17 = it.getCanonicalPath();
              boolean _contains_7 = _canonicalPath_17.contains("jOOQ-parse");
              boolean _not_14 = (!_contains_7);
              _and_8 = _not_14;
            }
            if (!_and_8) {
              _and_7 = false;
            } else {
              String _canonicalPath_18 = it.getCanonicalPath();
              boolean _contains_8 = _canonicalPath_18.contains("\\target\\");
              boolean _not_15 = (!_contains_8);
              _and_7 = _not_15;
            }
            if (!_and_7) {
              _and_6 = false;
            } else {
              String _canonicalPath_19 = it.getCanonicalPath();
              boolean _contains_9 = _canonicalPath_19.contains("\\bin\\");
              boolean _not_16 = (!_contains_9);
              _and_6 = _not_16;
            }
            if (!_and_6) {
              _and_5 = false;
            } else {
              String _canonicalPath_20 = it.getCanonicalPath();
              boolean _contains_10 = _canonicalPath_20.contains("\\.idea\\");
              boolean _not_17 = (!_contains_10);
              _and_5 = _not_17;
            }
            if (!_and_5) {
              _and_4 = false;
            } else {
              String _canonicalPath_21 = it.getCanonicalPath();
              boolean _contains_11 = _canonicalPath_21.contains("\\.settings");
              boolean _not_18 = (!_contains_11);
              _and_4 = _not_18;
            }
            if (!_and_4) {
              _and_3 = false;
            } else {
              String _canonicalPath_22 = it.getCanonicalPath();
              boolean _contains_12 = _canonicalPath_22.contains("\\.project");
              boolean _not_19 = (!_contains_12);
              _and_3 = _not_19;
            }
            if (!_and_3) {
              _and_2 = false;
            } else {
              String _canonicalPath_23 = it.getCanonicalPath();
              boolean _endsWith_7 = _canonicalPath_23.endsWith("\\.classpath");
              boolean _not_20 = (!_endsWith_7);
              _and_2 = _not_20;
            }
            if (!_and_2) {
              _and_1 = false;
            } else {
              String _canonicalPath_24 = it.getCanonicalPath();
              boolean _contains_13 = _canonicalPath_24.contains("\\Sakila\\");
              boolean _not_21 = (!_contains_13);
              _and_1 = _not_21;
            }
            if (!_and_1) {
              _and = false;
            } else {
              boolean _or = false;
              boolean _contains_14 = this.tokens.contains("trial");
              if (_contains_14) {
                _or = true;
              } else {
                boolean _and_22 = false;
                boolean _and_23 = false;
                boolean _and_24 = false;
                boolean _and_25 = false;
                boolean _and_26 = false;
                boolean _and_27 = false;
                boolean _and_28 = false;
                boolean _and_29 = false;
                boolean _and_30 = false;
                boolean _and_31 = false;
                boolean _and_32 = false;
                boolean _and_33 = false;
                boolean _and_34 = false;
                String _canonicalPath_25 = it.getCanonicalPath();
                boolean _contains_15 = _canonicalPath_25.contains("\\access\\");
                boolean _not_22 = (!_contains_15);
                if (!_not_22) {
                  _and_34 = false;
                } else {
                  String _canonicalPath_26 = it.getCanonicalPath();
                  boolean _contains_16 = _canonicalPath_26.contains("\\ase\\");
                  boolean _not_23 = (!_contains_16);
                  _and_34 = _not_23;
                }
                if (!_and_34) {
                  _and_33 = false;
                } else {
                  String _canonicalPath_27 = it.getCanonicalPath();
                  boolean _contains_17 = _canonicalPath_27.contains("\\db2\\");
                  boolean _not_24 = (!_contains_17);
                  _and_33 = _not_24;
                }
                if (!_and_33) {
                  _and_32 = false;
                } else {
                  String _canonicalPath_28 = it.getCanonicalPath();
                  boolean _contains_18 = _canonicalPath_28.contains("\\hana\\");
                  boolean _not_25 = (!_contains_18);
                  _and_32 = _not_25;
                }
                if (!_and_32) {
                  _and_31 = false;
                } else {
                  String _canonicalPath_29 = it.getCanonicalPath();
                  boolean _contains_19 = _canonicalPath_29.contains("\\informix\\");
                  boolean _not_26 = (!_contains_19);
                  _and_31 = _not_26;
                }
                if (!_and_31) {
                  _and_30 = false;
                } else {
                  String _canonicalPath_30 = it.getCanonicalPath();
                  boolean _contains_20 = _canonicalPath_30.contains("\\ingres\\");
                  boolean _not_27 = (!_contains_20);
                  _and_30 = _not_27;
                }
                if (!_and_30) {
                  _and_29 = false;
                } else {
                  String _canonicalPath_31 = it.getCanonicalPath();
                  boolean _contains_21 = _canonicalPath_31.contains("\\jdbcoracle\\");
                  boolean _not_28 = (!_contains_21);
                  _and_29 = _not_28;
                }
                if (!_and_29) {
                  _and_28 = false;
                } else {
                  String _canonicalPath_32 = it.getCanonicalPath();
                  boolean _contains_22 = _canonicalPath_32.contains("\\redshift\\");
                  boolean _not_29 = (!_contains_22);
                  _and_28 = _not_29;
                }
                if (!_and_28) {
                  _and_27 = false;
                } else {
                  String _canonicalPath_33 = it.getCanonicalPath();
                  boolean _contains_23 = _canonicalPath_33.contains("\\oracle\\");
                  boolean _not_30 = (!_contains_23);
                  _and_27 = _not_30;
                }
                if (!_and_27) {
                  _and_26 = false;
                } else {
                  String _canonicalPath_34 = it.getCanonicalPath();
                  boolean _contains_24 = _canonicalPath_34.contains("\\oracle2\\");
                  boolean _not_31 = (!_contains_24);
                  _and_26 = _not_31;
                }
                if (!_and_26) {
                  _and_25 = false;
                } else {
                  String _canonicalPath_35 = it.getCanonicalPath();
                  boolean _contains_25 = _canonicalPath_35.contains("\\oracle3\\");
                  boolean _not_32 = (!_contains_25);
                  _and_25 = _not_32;
                }
                if (!_and_25) {
                  _and_24 = false;
                } else {
                  String _canonicalPath_36 = it.getCanonicalPath();
                  boolean _contains_26 = _canonicalPath_36.contains("\\sqlserver\\");
                  boolean _not_33 = (!_contains_26);
                  _and_24 = _not_33;
                }
                if (!_and_24) {
                  _and_23 = false;
                } else {
                  String _canonicalPath_37 = it.getCanonicalPath();
                  boolean _contains_27 = _canonicalPath_37.contains("\\sybase\\");
                  boolean _not_34 = (!_contains_27);
                  _and_23 = _not_34;
                }
                if (!_and_23) {
                  _and_22 = false;
                } else {
                  String _canonicalPath_38 = it.getCanonicalPath();
                  boolean _contains_28 = _canonicalPath_38.contains("\\vertica\\");
                  boolean _not_35 = (!_contains_28);
                  _and_22 = _not_35;
                }
                _or = _and_22;
              }
              _and = _or;
            }
            return _and;
          } catch (Throwable _e) {
            throw Exceptions.sneakyThrow(_e);
          }
        };
        final File[] files = in.listFiles(_function);
        for (final File file : files) {
          this.transform(inRoot, outRoot, file);
        }
      } else {
        boolean _and = false;
        boolean _contains = this.tokens.contains("pro");
        if (!_contains) {
          _and = false;
        } else {
          boolean _or = false;
          String _name = in.getName();
          boolean _equals = _name.equals("LICENSE.txt");
          if (_equals) {
            _or = true;
          } else {
            String _name_1 = in.getName();
            boolean _equals_1 = _name_1.equals("LICENSE");
            _or = _equals_1;
          }
          _and = _or;
        }
        if (_and) {
          final Runnable _function_1 = () -> {
            StringConcatenation _builder = new StringConcatenation();
            _builder.append("Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)");
            _builder.newLine();
            _builder.append("All rights reserved.");
            _builder.newLine();
            _builder.newLine();
            _builder.append("Licensed under the Apache License, Version 2.0 (the \"License\");");
            _builder.newLine();
            _builder.append("you may not use this file except in compliance with the License.");
            _builder.newLine();
            _builder.append("You may obtain a copy of the License at");
            _builder.newLine();
            _builder.newLine();
            _builder.append(" ");
            _builder.append("http://www.apache.org/licenses/LICENSE-2.0");
            _builder.newLine();
            _builder.newLine();
            _builder.append("Unless required by applicable law or agreed to in writing, software");
            _builder.newLine();
            _builder.append("distributed under the License is distributed on an \"AS IS\" BASIS,");
            _builder.newLine();
            _builder.append("WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
            _builder.newLine();
            _builder.append("See the License for the specific language governing permissions and");
            _builder.newLine();
            _builder.append("limitations under the License.");
            _builder.newLine();
            _builder.newLine();
            _builder.append("Other licenses:");
            _builder.newLine();
            _builder.append("-----------------------------------------------------------------------------");
            _builder.newLine();
            _builder.append("Commercial licenses for this work are available. These replace the above");
            _builder.newLine();
            _builder.append("ASL 2.0 and offer limited warranties, support, maintenance, and commercial");
            _builder.newLine();
            _builder.append("database integrations.");
            _builder.newLine();
            _builder.newLine();
            _builder.append("For more information, please visit: http://www.jooq.org/licenses");
            this.write(out, _builder);
          };
          Splitter.ex.submit(_function_1);
        } else {
          final Runnable _function_2 = () -> {
            String original = this.read(in);
            String content = original;
            for (final Pattern pattern : this.translateAll) {
              {
                final Matcher m = pattern.matcher(content);
                while (m.find()) {
                  int _start = m.start();
                  String _substring = content.substring(0, _start);
                  String _group = m.group(1);
                  String _plus_2 = (_substring + _group);
                  String _group_1 = m.group(2);
                  String _replaceAll = _group_1.replaceAll("\\S", "x");
                  String _plus_3 = (_plus_2 + _replaceAll);
                  String _group_2 = m.group(3);
                  String _plus_4 = (_plus_3 + _group_2);
                  int _end = m.end();
                  String _substring_1 = content.substring(_end);
                  String _plus_5 = (_plus_4 + _substring_1);
                  content = _plus_5;
                }
              }
            }
            for (final AbstractMap.SimpleImmutableEntry<Pattern, String> pair : this.replaceFirst) {
              Pattern _key = pair.getKey();
              Matcher _matcher = _key.matcher(content);
              String _value = pair.getValue();
              String _replaceAll = _matcher.replaceAll(_value);
              content = _replaceAll;
            }
            for (final AbstractMap.SimpleImmutableEntry<Pattern, String> pair_1 : this.replaceAll) {
              Pattern _key_1 = pair_1.getKey();
              Matcher _matcher_1 = _key_1.matcher(content);
              String _value_1 = pair_1.getValue();
              String _replaceAll_1 = _matcher_1.replaceAll(_value_1);
              content = _replaceAll_1;
            }
            this.write(out, content);
            this.compare(content, original);
          };
          Splitter.ex.submit(_function_2);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void compare(final String content, final String original) {
    int _length = original.length();
    Splitter.charsTotal.addAndGet(_length);
    int _length_1 = content.length();
    int _length_2 = original.length();
    int _min = Math.min(_length_1, _length_2);
    IntegerRange _upTo = new IntegerRange(0, _min);
    for (final Integer i : _upTo) {
      boolean _and = false;
      char _charAt = content.charAt((i).intValue());
      String _plus = ("" + Character.valueOf(_charAt));
      boolean _equals = Objects.equal(_plus, "x");
      if (!_equals) {
        _and = false;
      } else {
        char _charAt_1 = original.charAt((i).intValue());
        String _plus_1 = ("" + Character.valueOf(_charAt_1));
        boolean _notEquals = (!Objects.equal(_plus_1, "x"));
        _and = _notEquals;
      }
      if (_and) {
        Splitter.charsMasked.incrementAndGet();
      }
    }
  }
  
  private final ArrayList<Pattern> translateAll = new ArrayList<Pattern>();
  
  private final ArrayList<AbstractMap.SimpleImmutableEntry<Pattern, String>> replaceAll = new ArrayList<AbstractMap.SimpleImmutableEntry<Pattern, String>>();
  
  private final ArrayList<AbstractMap.SimpleImmutableEntry<Pattern, String>> replaceFirst = new ArrayList<AbstractMap.SimpleImmutableEntry<Pattern, String>>();
  
  public Splitter(final String... tokens) {
    List<String> _asList = Arrays.<String>asList(tokens);
    this.tokens = _asList;
    boolean _contains = ((List<String>)Conversions.doWrapArray(tokens)).contains("pro");
    if (_contains) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("-trial\\.jar");
      Pattern _compile = Pattern.compile(_builder.toString());
      StringConcatenation _builder_1 = new StringConcatenation();
      _builder_1.append(".jar");
      AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile, _builder_1.toString());
      this.replaceFirst.add(_simpleImmutableEntry);
    } else {
      boolean _contains_1 = ((List<String>)Conversions.doWrapArray(tokens)).contains("java-8");
      if (_contains_1) {
        StringConcatenation _builder_2 = new StringConcatenation();
        _builder_2.append("-trial\\.jar");
        Pattern _compile_1 = Pattern.compile(_builder_2.toString());
        StringConcatenation _builder_3 = new StringConcatenation();
        _builder_3.append("-pro-java-6.jar");
        AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_1 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_1, _builder_3.toString());
        this.replaceFirst.add(_simpleImmutableEntry_1);
      } else {
        StringConcatenation _builder_4 = new StringConcatenation();
        _builder_4.append("-trial\\.jar");
        Pattern _compile_2 = Pattern.compile(_builder_4.toString());
        StringConcatenation _builder_5 = new StringConcatenation();
        _builder_5.append("-pro.jar");
        AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_2 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_2, _builder_5.toString());
        this.replaceFirst.add(_simpleImmutableEntry_2);
      }
    }
    boolean _contains_2 = ((List<String>)Conversions.doWrapArray(tokens)).contains("pro");
    if (_contains_2) {
      StringConcatenation _builder_6 = new StringConcatenation();
      _builder_6.append("import (org\\.jooq\\.(ArrayConstant|ArrayRecord|VersionsBetweenAndStep|impl\\.ArrayRecordImpl|impl\\.FlashbackTable.*?)|(com.microsoft.*?));");
      Pattern _compile_3 = Pattern.compile(_builder_6.toString());
      AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_3 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_3, "// ...");
      this.replaceFirst.add(_simpleImmutableEntry_3);
      StringConcatenation _builder_7 = new StringConcatenation();
      _builder_7.append("import static org\\.jooq\\.impl\\.DSL\\.(cube|grouping|groupingId|groupingSets);");
      Pattern _compile_4 = Pattern.compile(_builder_7.toString());
      AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_4 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_4, "// ...");
      this.replaceFirst.add(_simpleImmutableEntry_4);
      StringConcatenation _builder_8 = new StringConcatenation();
      _builder_8.append("(?s:/\\*\\*\\R \\* Copyright.*?Data Geekery GmbH.*?\\R \\*/)");
      Pattern _compile_5 = Pattern.compile(_builder_8.toString());
      StringConcatenation _builder_9 = new StringConcatenation();
      _builder_9.append("/**");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* Copyright (c) 2009-2015, Data Geekery GmbH (http://www.datageekery.com)");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* All rights reserved.");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* Licensed under the Apache License, Version 2.0 (the \"License\");");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* you may not use this file except in compliance with the License.");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* You may obtain a copy of the License at");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*  http://www.apache.org/licenses/LICENSE-2.0");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* Unless required by applicable law or agreed to in writing, software");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* distributed under the License is distributed on an \"AS IS\" BASIS,");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* See the License for the specific language governing permissions and");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* limitations under the License.");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* Other licenses:");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* -----------------------------------------------------------------------------");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* Commercial licenses for this work are available. These replace the above");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* ASL 2.0 and offer limited warranties, support, maintenance, and commercial");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* database integrations.");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("* For more information, please visit: http://www.jooq.org/licenses");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*");
      _builder_9.newLine();
      _builder_9.append(" ");
      _builder_9.append("*/");
      AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_5 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_5, _builder_9.toString());
      this.replaceFirst.add(_simpleImmutableEntry_5);
      SQLDialect[] _values = SQLDialect.values();
      final Function1<SQLDialect, Boolean> _function = (SQLDialect it) -> {
        return Boolean.valueOf(it.commercial());
      };
      Iterable<SQLDialect> _filter = IterableExtensions.<SQLDialect>filter(((Iterable<SQLDialect>)Conversions.doWrapArray(_values)), _function);
      for (final SQLDialect d : _filter) {
        {
          StringConcatenation _builder_10 = new StringConcatenation();
          _builder_10.append("(?s:(\\@Support\\([^\\)]*?),\\s*\\b");
          String _name = d.name();
          _builder_10.append(_name, "");
          _builder_10.append("\\b([^\\)]*?\\)))");
          Pattern _compile_6 = Pattern.compile(_builder_10.toString());
          AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_6 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_6, "$1$2");
          this.replaceAll.add(_simpleImmutableEntry_6);
          StringConcatenation _builder_11 = new StringConcatenation();
          _builder_11.append("(?s:(\\@Support\\([^\\)]*?)\\b");
          String _name_1 = d.name();
          _builder_11.append(_name_1, "");
          _builder_11.append("\\b,\\s*([^\\)]*?\\)))");
          Pattern _compile_7 = Pattern.compile(_builder_11.toString());
          AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_7 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_7, "$1$2");
          this.replaceAll.add(_simpleImmutableEntry_7);
          StringConcatenation _builder_12 = new StringConcatenation();
          _builder_12.append("(?s:(\\@Support\\([^\\)]*?)\\s*\\b");
          String _name_2 = d.name();
          _builder_12.append(_name_2, "");
          _builder_12.append("\\b\\s*([^\\)]*?\\)))");
          Pattern _compile_8 = Pattern.compile(_builder_12.toString());
          AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_8 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_8, "$1$2");
          this.replaceAll.add(_simpleImmutableEntry_8);
          StringConcatenation _builder_13 = new StringConcatenation();
          _builder_13.append("(?:(assume(?:Family|Dialect)NotIn.*\\([^\\)]*?),\\s*\\b");
          String _name_3 = d.name();
          _builder_13.append(_name_3, "");
          _builder_13.append("\\b([^\\)]*\\)))");
          Pattern _compile_9 = Pattern.compile(_builder_13.toString());
          AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_9 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_9, "$1$2");
          this.replaceAll.add(_simpleImmutableEntry_9);
          StringConcatenation _builder_14 = new StringConcatenation();
          _builder_14.append("(?:(assume(?:Family|Dialect)NotIn.*\\([^\\)]*?)\\b");
          String _name_4 = d.name();
          _builder_14.append(_name_4, "");
          _builder_14.append("\\b,\\s*([^\\)]*\\)))");
          Pattern _compile_10 = Pattern.compile(_builder_14.toString());
          AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_10 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_10, "$1$2");
          this.replaceAll.add(_simpleImmutableEntry_10);
          StringConcatenation _builder_15 = new StringConcatenation();
          _builder_15.append("(?:(assume(?:Family|Dialect)NotIn.*\\([^\\)]*?)\\s*\\b");
          String _name_5 = d.name();
          _builder_15.append(_name_5, "");
          _builder_15.append("\\b\\s*([^\\)]*\\)))");
          Pattern _compile_11 = Pattern.compile(_builder_15.toString());
          AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_11 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_11, "$1$2");
          this.replaceAll.add(_simpleImmutableEntry_11);
          StringConcatenation _builder_16 = new StringConcatenation();
          _builder_16.append("(asList\\([^\\)]*?),\\s*\\b");
          String _name_6 = d.name();
          _builder_16.append(_name_6, "");
          _builder_16.append("\\b([^\\)]*?\\))");
          Pattern _compile_12 = Pattern.compile(_builder_16.toString());
          AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_12 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_12, "$1$2");
          this.replaceAll.add(_simpleImmutableEntry_12);
          StringConcatenation _builder_17 = new StringConcatenation();
          _builder_17.append("(asList\\([^\\)]*?)\\b");
          String _name_7 = d.name();
          _builder_17.append(_name_7, "");
          _builder_17.append("\\b,\\s*([^\\)]*?\\))");
          Pattern _compile_13 = Pattern.compile(_builder_17.toString());
          AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_13 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_13, "$1$2");
          this.replaceAll.add(_simpleImmutableEntry_13);
          StringConcatenation _builder_18 = new StringConcatenation();
          _builder_18.append("(asList\\([^\\)]*?)\\s*\\b");
          String _name_8 = d.name();
          _builder_18.append(_name_8, "");
          _builder_18.append("\\b\\s*([^\\)]*?\\))");
          Pattern _compile_14 = Pattern.compile(_builder_18.toString());
          AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_14 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_14, "$1$2");
          this.replaceAll.add(_simpleImmutableEntry_14);
          StringConcatenation _builder_19 = new StringConcatenation();
          _builder_19.append("import (static )?org\\.jooq\\.SQLDialect\\.");
          String _name_9 = d.name();
          _builder_19.append(_name_9, "");
          _builder_19.append(";");
          Pattern _compile_15 = Pattern.compile(_builder_19.toString());
          AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_15 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_15, "// ...");
          this.replaceAll.add(_simpleImmutableEntry_15);
          StringConcatenation _builder_20 = new StringConcatenation();
          _builder_20.append("import (static )?org\\.jooq\\.util\\.");
          String _name_10 = d.name();
          String _lowerCase = _name_10.toLowerCase();
          _builder_20.append(_lowerCase, "");
          _builder_20.append("\\..*?;");
          Pattern _compile_16 = Pattern.compile(_builder_20.toString());
          AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_16 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_16, "// ...");
          this.replaceAll.add(_simpleImmutableEntry_16);
          StringConcatenation _builder_21 = new StringConcatenation();
          _builder_21.append("import (static )?org\\.jooq\\..*?(\\b|(?<=_))");
          String _name_11 = d.name();
          String _upperCase = _name_11.toUpperCase();
          _builder_21.append(_upperCase, "");
          _builder_21.append("(\\b|(?=_)).*?;");
          Pattern _compile_17 = Pattern.compile(_builder_21.toString());
          AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_17 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_17, "// ...");
          this.replaceAll.add(_simpleImmutableEntry_17);
        }
      }
    }
    boolean _contains_3 = ((List<String>)Conversions.doWrapArray(tokens)).contains("java-8");
    if (_contains_3) {
      StringConcatenation _builder_10 = new StringConcatenation();
      _builder_10.append("import (static )?java\\.lang\\.AutoCloseable;");
      Pattern _compile_6 = Pattern.compile(_builder_10.toString());
      AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_6 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_6, "// ...");
      this.replaceAll.add(_simpleImmutableEntry_6);
      StringConcatenation _builder_11 = new StringConcatenation();
      _builder_11.append("import (static )?java\\.util\\.Optional;");
      Pattern _compile_7 = Pattern.compile(_builder_11.toString());
      AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_7 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_7, "// ...");
      this.replaceAll.add(_simpleImmutableEntry_7);
      StringConcatenation _builder_12 = new StringConcatenation();
      _builder_12.append("import (static )?java\\.util\\.Spliterator;");
      Pattern _compile_8 = Pattern.compile(_builder_12.toString());
      AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_8 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_8, "// ...");
      this.replaceAll.add(_simpleImmutableEntry_8);
      StringConcatenation _builder_13 = new StringConcatenation();
      _builder_13.append("import (static )?java\\.util\\.Spliterators;");
      Pattern _compile_9 = Pattern.compile(_builder_13.toString());
      AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_9 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_9, "// ...");
      this.replaceAll.add(_simpleImmutableEntry_9);
      StringConcatenation _builder_14 = new StringConcatenation();
      _builder_14.append("import (static )?java\\.util\\.stream\\..*?;");
      Pattern _compile_10 = Pattern.compile(_builder_14.toString());
      AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_10 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_10, "// ...");
      this.replaceAll.add(_simpleImmutableEntry_10);
      StringConcatenation _builder_15 = new StringConcatenation();
      _builder_15.append("import (static )?java\\.util\\.function\\..*?;");
      Pattern _compile_11 = Pattern.compile(_builder_15.toString());
      AbstractMap.SimpleImmutableEntry<Pattern, String> _simpleImmutableEntry_11 = new AbstractMap.SimpleImmutableEntry<Pattern, String>(_compile_11, "// ...");
      this.replaceAll.add(_simpleImmutableEntry_11);
    }
    for (final String token : tokens) {
      {
        StringConcatenation _builder_16 = new StringConcatenation();
        _builder_16.append("(?s:(/\\* \\[");
        _builder_16.append(token, "");
        _builder_16.append("\\])( \\*.*?/\\* )(\\[/");
        _builder_16.append(token, "");
        _builder_16.append("\\] \\*/))");
        Pattern _compile_12 = Pattern.compile(_builder_16.toString());
        this.translateAll.add(_compile_12);
        StringConcatenation _builder_17 = new StringConcatenation();
        _builder_17.append("(?s:(<!-- \\[");
        _builder_17.append(token, "");
        _builder_17.append("\\])( -->.*?<!-- )(\\[/");
        _builder_17.append(token, "");
        _builder_17.append("\\] -->))");
        Pattern _compile_13 = Pattern.compile(_builder_17.toString());
        this.translateAll.add(_compile_13);
        StringConcatenation _builder_18 = new StringConcatenation();
        _builder_18.append("(?s:(# \\[");
        _builder_18.append(token, "");
        _builder_18.append("\\])()(?:.*?)(# \\[/");
        _builder_18.append(token, "");
        _builder_18.append("\\]))");
        Pattern _compile_14 = Pattern.compile(_builder_18.toString());
        this.translateAll.add(_compile_14);
      }
    }
  }
}
