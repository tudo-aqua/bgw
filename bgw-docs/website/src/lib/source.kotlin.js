/**
 * @import {Grammar} from '@wooorm/starry-night'
 */

/** @type {Grammar} */
const grammar = {
  extensions: [".gradle.kts", ".kt", ".ktm", ".kts"],
  names: ["gradle-kotlin-dsl", "kotlin"],
  patterns: [
    { include: "#comments" },
    { include: "#package" },
    { include: "#imports" },
    { include: "#code" },
  ],
  repository: {
    annotations: {
      patterns: [
        {
          match:
            "@(?:file|property|field|get|set|receiver|param|setparam|delegate)\\s*:\\s*?[a-zA-Z_]\\w*",
          name: "meta.annotation.kotlin",
        },
        {
          begin: "@[a-zA-Z_]\\w*\\s*(\\()",
          beginCaptures: {
            1: { name: "punctuation.definition.arguments.begin.kotlin" },
          },
          end: "\\)",
          endCaptures: {
            0: { name: "punctuation.definition.arguments.end.kotlin" },
          },
          name: "meta.annotation.kotlin",
          patterns: [
            { include: "#code" },
            { match: ",", name: "punctuation.seperator.property.kotlin" },
          ],
        },
        { match: "@[a-zA-Z_]\\w*", name: "meta.annotation.kotlin" },
      ],
    },
    braces: {
      patterns: [
        {
          begin: "\\{",
          beginCaptures: {
            0: { name: "punctuation.section.group.begin.kotlin" },
          },
          end: "\\}",
          endCaptures: { 0: { name: "punctuation.section.group.end.kotlin" } },
          name: "meta.block.kotlin",
          patterns: [{ include: "#code" }],
        },
      ],
    },
    brackets: {
      patterns: [
        {
          begin: "\\[",
          beginCaptures: {
            0: { name: "punctuation.section.brackets.begin.kotlin" },
          },
          end: "\\]",
          endCaptures: {
            0: { name: "punctuation.section.brackets.end.kotlin" },
          },
          name: "meta.brackets.kotlin",
          patterns: [{ include: "#code" }],
        },
      ],
    },
    "builtin-functions": {
      patterns: [
        {
          captures: { 1: { name: "entity.name.type.class.kotlin" } },
          match:
            "\\b(apply|also|let|run|takeIf|takeWhile|takeUnless|with|print|println)\\b\\s*(?={|\\()",
        },
        {
          captures: { 1: { name: "support.function.kotlin" } },
          match:
            "\\b(arrayListOf|mutableListOf|listOf|mutableMapOf|mapOf|mutableSetOf|setOf)\\b\\s*(?={|\\()",
        },
      ],
    },
    "class-ident": {
      patterns: [
        { match: "\\b[A-Z_]\\w*\\b", name: "entity.name.type.class.kotlin" },
      ],
    },
    "class-literal": {
      patterns: [
        {
          begin:
            "(?=\\b(?:(?:(?:data|value)\\s+)?class|(?:data\\s+)?object|(?:(?:fun|value)\\s+)?interface)\\s+\\w+)\\b",
          end: "(?=\\}|$)",
          name: "meta.class.kotlin",
          patterns: [
            { include: "#keywords" },
            {
              begin:
                "\\b((?:(?:data|value)\\s+)?class|(?:data\\s+)?object|(?:(?:fun|value)\\s+)?interface)\\b\\s+(\\w+)",
              beginCaptures: {
                1: { name: "storage.modifier.kotlin" },
                2: { name: "entity.name.class.kotlin" },
              },
              end: "(?=\\(|\\{|$)",
              patterns: [
                { include: "#comments" },
                { include: "#annotations" },
                { include: "#types" },
              ],
            },
            {
              begin: "(\\()",
              beginCaptures: {
                0: { name: "punctuation.section.group.begin.kotlin" },
                1: { name: "punctuation.definition.parameters.begin.kotlin" },
              },
              end: "(\\))",
              endCaptures: {
                0: { name: "punctuation.section.group.end.kotlin" },
                1: { name: "punctuation.definition.parameters.end.kotlin" },
              },
              name: "meta.parameters.kotlin",
              patterns: [
                { include: "#class-parameter-list" },
                { include: "#comments" },
              ],
            },
            {
              begin: "\\{",
              beginCaptures: {
                0: { name: "punctuation.section.group.begin.kotlin" },
              },
              end: "\\}",
              endCaptures: {
                0: { name: "punctuation.section.group.end.kotlin" },
              },
              name: "meta.block.kotlin",
              patterns: [{ include: "#code" }],
            },
          ],
        },
      ],
      repository: {
        "class-parameter-list": {
          patterns: [
            { include: "#generic" },
            { include: "#annotations" },
            { include: "#keywords" },
            {
              captures: {
                1: { name: "variable.parameter.function.kotlin" },
                2: { name: "keyword.operator.declaration.kotlin" },
              },
              match: "(\\w+)\\s*(:)",
            },
            { match: ",", name: "punctuation.seperator.kotlin" },
            { include: "#types" },
            { include: "#literals" },
          ],
        },
      },
    },
    code: {
      patterns: [
        { include: "#comments" },
        { include: "#annotations" },
        { include: "#parens" },
        { include: "#braces" },
        { include: "#brackets" },
        { include: "#class-literal" },
        { include: "#literal-functions" },
        { include: "#literals" },
        { include: "#keywords" },
        { include: "#types" },
        { include: "#operators" },
        { include: "#constants" },
        { include: "#punctuations" },
        { include: "#builtin-functions" },
      ],
    },
    comments: {
      patterns: [
        { include: "#inline" },
        {
          begin: "/\\*",
          beginCaptures: {
            0: { name: "punctuation.definition.comment.begin.kotlin" },
          },
          end: "\\*/",
          endCaptures: {
            0: { name: "punctuation.definition.comment.end.kotlin" },
          },
          name: "comment.block.kotlin",
          patterns: [{ include: "#nested" }],
        },
      ],
      repository: {
        inline: {
          patterns: [
            {
              captures: {
                0: { name: "punctuation.definition.comment.kotlin" },
                1: { name: "comment.line.double-slash.kotlin" },
              },
              match: "(//).*$\\n?",
            },
          ],
        },
        nested: {
          patterns: [
            { begin: "/\\*", end: "\\*/", patterns: [{ include: "#nested" }] },
          ],
        },
      },
    },
    constants: {
      patterns: [
        { match: "\\b(class)\\b", name: "constant.language.kotlin" },
        { match: "\\b(this|super)\\b", name: "variable.language.kotlin" },
      ],
    },
    generic: {
      patterns: [
        {
          begin: "(?=\\<(?:[A-Z_]|\\*|in|out))",
          end: "(?<=\\>)(?!\\>)",
          patterns: [
            { match: "<", name: "punctuation.bracket.angle.begin.kotlin" },
            { match: ">", name: "punctuation.bracket.angle.end.kotlin" },
            { match: "\\*", name: "entity.name.type.generic.wildcard.kotlin" },
            { include: "#generic-parameter-list" },
            { match: ",", name: "punctuation.seperator.kotlin" },
          ],
        },
      ],
      repository: {
        "generic-parameter-list": {
          patterns: [
            { include: "#annotations" },
            {
              match: "\\b(in|out)\\b",
              name: "storage.modifier.generic.variance.kotlin",
            },
            { include: "#built-in-types" },
            { include: "#class-ident" },
            { include: "#generic" },
            { include: "#operators" },
          ],
        },
      },
    },
    imports: {
      patterns: [
        {
          captures: {
            1: { name: "keyword.other.import.kotlin" },
            2: { name: "storage.modifier.import.kotlin" },
            3: { name: "keyword.other.kotlin" },
            4: { name: "entity.name.type" },
          },
          match:
            "^\\s*(import)\\s+((?:[`][^$`]+[`]|[^` $.]+)(?:\\.(?:[`][^$`]+[`]|[^` $.]+))*)(?:\\s+(as)\\s+([`][^$`]+[`]|[^` $.]+))?",
          name: "meta.import.kotlin",
        },
      ],
    },
    keywords: {
      patterns: [
        { match: "(\\!in|\\!is|as\\?)\\b", name: "keyword.operator.kotlin" },
        { match: "\\b(in|is|as|assert)\\b", name: "keyword.operator.kotlin" },
        { match: "\\b(val|var)\\b", name: "storage.type.kotlin" },
        {
          match: "\\b(\\_)\\b",
          name: "punctuation.definition.variable.kotlin",
        },
        {
          match:
            "\\b(tailrec|operator|infix|typealias|reified|copy(?=\\s+fun|\\s+var))\\b",
          name: "storage.type.kotlin",
        },
        {
          match: "\\b(out|in|yield|typealias|override)\\b",
          name: "storage.modifier.kotlin",
        },
        {
          match:
            "\\b(?<![+-/%*=(,]\\s)(inline|inner|external|public|private|protected|internal|abstract|final|sealed|enum|open|annotation|expect|actual|const|lateinit)(?=\\s(?!(?:\\s*)(?:[+-/%*=:).,]|$)))\\b",
          name: "storage.modifier.kotlin",
        },
        {
          match: "\\b(vararg(?=\\s+\\w+:))\\b",
          name: "storage.modifier.kotlin",
        },
        {
          match: "\\b(suspend(?!\\s*[\\(]?\\s*\\{))\\b",
          name: "storage.modifier.kotlin",
        },
        {
          match: "\\b(try|catch|finally|throw)\\b",
          name: "keyword.control.catch-exception.kotlin",
        },
        {
          match: "\\b(if|else|when)\\b",
          name: "keyword.control.conditional.kotlin",
        },
        {
          match: "\\b(while|for|do|return|break|continue)\\b",
          name: "keyword.control.kotlin",
        },
        {
          match: "init",
          name: "meta.class.inheritance.kotlin",
        },
        {
          match: "\\b(constructor)\\b",
          name: "keyword.control.kotlin",
        },
        { match: "\\b(companion|object)\\b", name: "storage.type.kotlin" },
      ],
    },
    "literal-functions": {
      patterns: [
        {
          begin: "(?=\\b(?:fun)\\b)",
          end: "(?<=$|=|\\})",
          name: "meta.function.kotlin",
          patterns: [
            { include: "#keywords" },
            {
              begin: "\\bfun\\b",
              beginCaptures: { 0: { name: "keyword.other.kotlin" } },
              end: "(?=\\()",
              patterns: [
                { include: "#generic" },
                {
                  captures: {
                    0: { name: "entity.name.function.kotlin" },
                    1: { name: "string.quoted.backtick.kotlin" },
                  },
                  match: "(`[^`]*`)",
                },
                {
                  captures: { 2: { name: "entity.name.function.kotlin" } },
                  match: "([\\.<\\?>\\w]+\\.)?(\\w+)",
                },
                { include: "#types" },
              ],
            },
            {
              begin: "(\\()",
              beginCaptures: {
                0: { name: "punctuation.section.group.begin.kotlin" },
                1: { name: "punctuation.definition.parameters.begin.kotlin" },
              },
              end: "(\\))",
              endCaptures: {
                0: { name: "punctuation.section.group.end.kotlin" },
                1: { name: "punctuation.definition.parameters.end.kotlin" },
              },
              name: "meta.parameters.kotlin",
              patterns: [{ include: "#function-parameter-list" }],
            },
            { match: "=", name: "keyword.operator.single-expression.kotlin" },
            {
              begin: "\\{",
              beginCaptures: {
                0: { name: "punctuation.section.group.begin.kotlin" },
              },
              end: "\\}",
              endCaptures: {
                0: { name: "punctuation.section.group.end.kotlin" },
              },
              name: "meta.block.kotlin",
              patterns: [{ include: "#code" }],
            },
            { include: "#return-type" },
          ],
        },
      ],
      repository: {
        "function-parameter-list": {
          patterns: [
            { include: "#comments" },
            { include: "#annotations" },
            { include: "#keywords" },
            {
              captures: {
                1: { name: "variable.parameter.function.kotlin" },
                2: { name: "keyword.operator.declaration.kotlin" },
              },
              match: "(\\w+)\\s*(:)",
            },
            { match: ",", name: "punctuation.seperator.kotlin" },
            { include: "#types" },
          ],
        },
        "return-type": {
          patterns: [
            {
              begin: "(?<=\\))\\s*(:)(?=\\s*\\S)",
              end: "(?<![:|&])(?=$|^|[={};,]|//)",
              name: "meta.return.type.kotlin",
              patterns: [{ include: "#types" }],
            },
          ],
        },
      },
    },
    literals: {
      patterns: [
        { include: "#boolean" },
        { include: "#numeric" },
        { include: "#string" },
        { include: "#null" },
      ],
      repository: {
        boolean: {
          patterns: [
            {
              match: "\\b(true|false)\\b",
              name: "constant.language.boolean.kotlin",
            },
          ],
        },
        null: {
          patterns: [
            { match: "\\b(null)\\b", name: "constant.language.null.kotlin" },
          ],
        },
        numeric: {
          patterns: [
            {
              match: "\\b(0(x|X)[0-9A-Fa-f_]*)([LuU]|[uU]L)?\\b",
              name: "constant.numeric.hex.kotlin",
            },
            {
              match: "\\b(0(b|B)[0-1_]*)([LuU]|[uU]L)?\\b",
              name: "constant.numeric.binary.kotlin",
            },
            {
              match: "\\b([0-9][0-9_]*\\.[0-9][0-9_]*[fFL]?)\\b",
              name: "constant.numeric.float.kotlin",
            },
            {
              match: "\\b([0-9][0-9_]*([fFLuU]|[uU]L)?)\\b",
              name: "constant.numeric.integer.kotlin",
            },
          ],
        },
        string: {
          patterns: [
            {
              begin: '"""',
              beginCaptures: {
                0: { name: "punctuation.definition.string.begin.kotlin" },
              },
              end: '"""(?!")',
              endCaptures: {
                0: { name: "punctuation.definition.string.end.kotlin" },
              },
              name: "string.quoted.triple.kotlin",
              patterns: [{ include: "#raw-string-content" }],
            },
            {
              begin: "(?!')\"",
              beginCaptures: {
                0: { name: "punctuation.definition.string.begin.kotlin" },
              },
              end: '"',
              endCaptures: {
                0: { name: "punctuation.definition.string.end.kotlin" },
              },
              name: "string.quoted.double.kotlin",
              patterns: [{ include: "#string-content" }],
            },
            {
              begin: "'",
              beginCaptures: {
                0: { name: "punctuation.definition.string.begin.kotlin" },
              },
              end: "'",
              endCaptures: {
                0: { name: "punctuation.definition.string.end.kotlin" },
              },
              name: "string.quoted.single.kotlin",
              patterns: [{ include: "#string-content" }],
            },
          ],
          repository: {
            "raw-string-content": {
              patterns: [
                {
                  begin: "\\$(\\{)",
                  beginCaptures: {
                    1: { name: "punctuation.section.block.begin.kotlin" },
                  },
                  end: "\\}",
                  endCaptures: {
                    0: { name: "punctuation.section.block.end.kotlin" },
                  },
                  name: "entity.string.template.element.kotlin",
                  patterns: [{ include: "#code" }],
                },
                {
                  match: "\\$[a-zA-Z_]\\w*",
                  name: "entity.string.template.element.kotlin",
                },
              ],
            },
            "string-content": {
              patterns: [
                {
                  match: "\\\\[0\\\\tnr\"']",
                  name: "constant.character.escape.kotlin",
                },
                {
                  match: "\\\\(x[\\da-fA-F]{2}|u[\\da-fA-F]{4}|.)",
                  name: "constant.character.escape.unicode.kotlin",
                },
                {
                  begin: "\\$(\\{)",
                  beginCaptures: {
                    1: { name: "punctuation.section.block.begin.kotlin" },
                  },
                  end: "\\}",
                  endCaptures: {
                    0: { name: "punctuation.section.block.end.kotlin" },
                  },
                  name: "entity.string.template.element.kotlin",
                  patterns: [{ include: "#code" }],
                },
                {
                  match: "\\$[a-zA-Z_]\\w*",
                  name: "entity.string.template.element.kotlin",
                },
              ],
            },
          },
        },
      },
    },
    "object-literal": {
      patterns: [
        {
          begin: "(?=\\b(?:object)\\b((\\s*:\\s*)|\\s+)\\w+)",
          end: "(?=\\}|$)",
          name: "meta.class.kotlin",
          patterns: [
            { include: "#annotation" },
            {
              begin: "\\b(object)\\b\\s*(:)\\s*(\\w+)",
              beginCaptures: {
                1: { name: "storage.modifier.kotlin" },
                2: { name: "keyword.operator.declaration.kotlin" },
                3: { name: "entity.name.class.kotlin" },
              },
              end: "(?=\\(|\\{|$)",
              patterns: [
                { include: "#comments" },
                { include: "#annotations" },
                { include: "#types" },
              ],
            },
            {
              begin: "(\\()",
              beginCaptures: {
                0: { name: "punctuation.section.group.begin.kotlin" },
                1: { name: "punctuation.definition.parameters.begin.kotlin" },
              },
              end: "(\\))",
              endCaptures: {
                0: { name: "punctuation.section.group.end.kotlin" },
                1: { name: "punctuation.definition.parameters.end.kotlin" },
              },
              name: "meta.parameters.kotlin",
              patterns: [
                { include: "#comments" },
                { include: "#class-parameter-list" },
              ],
            },
            {
              begin: "\\{",
              beginCaptures: {
                0: { name: "punctuation.section.group.begin.kotlin" },
              },
              end: "\\}",
              endCaptures: {
                0: { name: "punctuation.section.group.end.kotlin" },
              },
              name: "meta.block.kotlin",
              patterns: [{ include: "#code" }],
            },
          ],
        },
      ],
      repository: {
        "class-parameter-list": {
          patterns: [
            { include: "#annotations" },
            { include: "#keywords" },
            {
              captures: {
                1: { name: "variable.parameter.function.kotlin" },
                2: { name: "keyword.operator.declaration.kotlin" },
              },
              match: "(\\w+)\\s*(:)",
            },
            { match: ",", name: "punctuation.seperator.kotlin" },
            { include: "#types" },
          ],
        },
      },
    },
    operators: {
      patterns: [
        {
          match: "\\b(and|or|not|inv)\\b",
          name: "keyword.operator.bitwise.kotlin",
        },
        {
          match: "(==|!=|===|!==|<=|>=|<|>)",
          name: "keyword.operator.comparison.kotlin",
        },
        { match: "(=)", name: "" },
        { match: "(:(?!:))", name: "keyword.operator.declaration.kotlin" },
        { match: "(\\?:)", name: "keyword.operator.elvis.kotlin" },
        {
          match: "(\\-\\-|\\+\\+)",
          name: "keyword.operator.increment-decrement.kotlin",
        },
        {
          match: "(\\-|\\+|\\*|\\/|%)",
          name: "keyword.operator.arithmetic.kotlin",
        },
        {
          match: "(\\+\\=|\\-\\=|\\*\\=|\\/\\=)",
          name: "keyword.operator.arithmetic.assign.kotlin",
        },
        {
          match: "(\\!|\\&\\&|\\|\\|)",
          name: "keyword.operator.logical.kotlin",
        },
        { match: "(\\.\\.)", name: "keyword.operator.range.kotlin" },
      ],
    },
    package: {
      patterns: [
        {
          captures: {
            1: { name: "keyword.other.kotlin" },
            2: { name: "entity.name.package.kotlin" },
          },
          match: "^\\s*(package)\\b(?:\\s*([^ ;$]+)\\s*)?",
        },
      ],
    },
    parens: {
      patterns: [
        {
          begin: "\\(",
          beginCaptures: {
            0: { name: "punctuation.section.group.begin.kotlin" },
          },
          end: "\\)",
          endCaptures: { 0: { name: "punctuation.section.group.end.kotlin" } },
          name: "meta.group.kotlin",
          patterns: [{ include: "#code" }],
        },
      ],
    },
    punctuations: {
      patterns: [
        { match: "::", name: "punctuation.accessor.reference.kotlin" },
        { match: "\\?\\.", name: "punctuation.accessor.dot.safe.kotlin" },
        { match: "(?<!\\?)\\.", name: "punctuation.accessor.dot.kotlin" },
        { match: "\\,", name: "punctuation.seperator.kotlin" },
        { match: "\\;", name: "punctuation.terminator.kotlin" },
      ],
    },
    types: {
      patterns: [
        { include: "#built-in-types" },
        { include: "#class-ident" },
        { include: "#generic" },
        {
          captures: { 1: { name: "keyword.operator.type.function.kotlin" } },
          match: "(?<![/=\\-+!*%<>&|\\^~.])(->)(?![/=\\-+!*%<>&|\\^~.])",
        },
        { match: "\\?(?!\\.)", name: "keyword.operator.type.nullable.kotlin" },
        {
          begin: "\\(",
          beginCaptures: {
            0: { name: "punctuation.section.group.begin.kotlin" },
          },
          end: "\\)",
          endCaptures: { 0: { name: "punctuation.section.group.end.kotlin" } },
          patterns: [{ include: "#types" }],
        },
      ],
      repository: {
        "built-in-types": {
          patterns: [
            {
              match:
                "\\b(Nothing|Any|Unit|String|CharSequence|Int|Boolean|Char|Long|Double|Float|Short|Byte|UByte|UShort|UInt|ULong|Array|List|Map|Set|dynamic)\\b(\\?)?",
              name: "support.class.kotlin",
            },
            {
              match:
                "\\b(IntArray|BooleanArray|CharArray|LongArray|DoubleArray|FloatArray|ShortArray|ByteArray|UByteArray|UShortArray|UIntArray|ULongArray)\\b(\\?)?",
              name: "support.class.kotlin",
            },
          ],
        },
      },
    },
  },
  scopeName: "source.kotlin",
};

export default grammar;
