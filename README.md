# Intellij PHP File Structure Exporter

(poc) Intellij plugin that extracts PHP file structure with code usages like class, interfaces, annotations.
Also supports Symfony service because it fully reusage Intellij external Plugins.

## Example

```javascript
{
   "items":[
      {
         "context":{
            "start_line":10,
            "context":[
               {
                  "content":"",
                  "line":10
               },
               {
                  "content":"namespace Symfony\\Cmf\\Component\\Routing\\DependencyInjection\\Compiler;",
                  "line":11
               },
               {
                  "content":"",
                  "line":12
               },
               {
                  "content":"use Symfony\\Component\\DependencyInjection\\ContainerBuilder;",
                  "is_highlight":true,
                  "line":13
               },
               {
                  "content":"use Symfony\\Component\\DependencyInjection\\Reference;",
                  "line":14
               },
               {
                  "content":"use Symfony\\Component\\DependencyInjection\\Compiler\\CompilerPassInterface;",
                  "line":15
               },
               {
                  "content":"",
                  "line":16
               }
            ],
            "line":13,
            "end_line":16
         },
         "class":"Symfony\\Component\\DependencyInjection\\ContainerBuilder",
         "type":"use",
         "key":"301-355"
      },
      {
         "context":{
            "start_line":11,
            "context":[
               {
                  "content":"namespace Symfony\\Cmf\\Component\\Routing\\DependencyInjection\\Compiler;",
                  "line":11
               },
               {
                  "content":"",
                  "line":12
               },
               {
                  "content":"use Symfony\\Component\\DependencyInjection\\ContainerBuilder;",
                  "line":13
               },
               {
                  "content":"use Symfony\\Component\\DependencyInjection\\Reference;",
                  "is_highlight":true,
                  "line":14
               },
               {
                  "content":"use Symfony\\Component\\DependencyInjection\\Compiler\\CompilerPassInterface;",
                  "line":15
               },
               {
                  "content":"",
                  "line":16
               },
               {
                  "content":"/**",
                  "line":17
               }
            ],
            "line":14,
            "end_line":17
         },
         "class":"Symfony\\Component\\DependencyInjection\\Reference",
         "type":"use",
         "key":"361-408"
      },
      {
         "context":{
            "start_line":12,
            "context":[
               {
                  "content":"",
                  "line":12
               },
               {
                  "content":"use Symfony\\Component\\DependencyInjection\\ContainerBuilder;",
                  "line":13
               },
               {
                  "content":"use Symfony\\Component\\DependencyInjection\\Reference;",
                  "line":14
               },
               {
                  "content":"use Symfony\\Component\\DependencyInjection\\Compiler\\CompilerPassInterface;",
                  "is_highlight":true,
                  "line":15
               },
               {
                  "content":"",
                  "line":16
               },
               {
                  "content":"/**",
                  "line":17
               },
               {
                  "content":" * Compiler pass to register routers to the ChainRouter.",
                  "line":18
               }
            ],
            "line":15,
            "end_line":18
         },
         "class":"Symfony\\Component\\DependencyInjection\\Compiler\\CompilerPassInterface",
         "type":"use",
         "key":"414-482"
      },
      {
         "doc_comment":"/**\n * Compiler pass to register routers to the ChainRouter.\n *\n * @author Wouter J <waldio.webdesign@gmail.com>\n * @author Henrik Bjornskov <henrik@bjrnskov.dk>\n * @author Magnus Nordlander <magnus@e-butik.se>\n */",
         "class":"Symfony\\Cmf\\Component\\Routing\\DependencyInjection\\Compiler\\RegisterRoutersPass",
         "type":"class"
      },
      {
         "child":"Symfony\\Component\\DependencyInjection\\Compiler\\CompilerPassInterface",
         "is_interface":false,
         "class":"Symfony\\Cmf\\Component\\Routing\\DependencyInjection\\Compiler\\RegisterRoutersPass",
         "type":"implements"
      },
      {
         "child":"Symfony\\Component\\DependencyInjection\\Compiler\\CompilerPassInterface",
         "is_interface":false,
         "class":"Symfony\\Cmf\\Component\\Routing\\DependencyInjection\\Compiler\\RegisterRoutersPass",
         "type":"implements"
      },
      {
         "child":"Symfony\\Component\\DependencyInjection\\Compiler\\CompilerPassInterface",
         "is_interface":false,
         "class":"Symfony\\Cmf\\Component\\Routing\\DependencyInjection\\Compiler\\RegisterRoutersPass",
         "type":"implements"
      },
      {
         "child":"Symfony\\Component\\DependencyInjection\\Compiler\\CompilerPassInterface",
         "is_interface":false,
         "class":"Symfony\\Cmf\\Component\\Routing\\DependencyInjection\\Compiler\\RegisterRoutersPass",
         "type":"implements"
      },
      {
         "child":"Symfony\\Component\\DependencyInjection\\Compiler\\CompilerPassInterface",
         "is_interface":false,
         "class":"Symfony\\Cmf\\Component\\Routing\\DependencyInjection\\Compiler\\RegisterRoutersPass",
         "type":"implements"
      },
      {
         "is_interface":false,
         "context":{
            "start_line":36,
            "context":[
               {
                  "content":"        $this->routerTag = $routerTag;",
                  "line":36
               },
               {
                  "content":"    }",
                  "line":37
               },
               {
                  "content":"",
                  "line":38
               },
               {
                  "content":"    public function process(ContainerBuilder $container)",
                  "is_highlight":true,
                  "line":39
               },
               {
                  "content":"    {",
                  "line":40
               },
               {
                  "content":"        if (!$container->hasDefinition($this->chainRouterService)) {",
                  "line":41
               },
               {
                  "content":"            return;",
                  "line":42
               }
            ],
            "line":39,
            "end_line":42
         },
         "class":"Symfony\\Component\\DependencyInjection\\ContainerBuilder",
         "type":"type_hint",
         "key":"1095-1111"
      },
      {
         "is_interface":false,
         "context":{
            "start_line":47,
            "context":[
               {
                  "content":"        foreach ($container->findTaggedServiceIds($this->routerTag) as $id => $attributes) {",
                  "line":47
               },
               {
                  "content":"            $priority = isset($attributes[0]['priority']) ? $attributes[0]['priority'] : 0;",
                  "line":48
               },
               {
                  "content":"",
                  "line":49
               },
               {
                  "content":"            $definition->addMethodCall('add', array(new Reference($id), $priority));",
                  "is_highlight":true,
                  "line":50
               },
               {
                  "content":"        }",
                  "line":51
               },
               {
                  "content":"    }",
                  "line":52
               },
               {
                  "content":"}",
                  "line":53
               }
            ],
            "line":50,
            "end_line":53
         },
         "class":"Symfony\\Component\\DependencyInjection\\Reference",
         "type":"instance",
         "key":"1545-1563"
      }
   ],
   "file":"DependencyInjection/Compiler/RegisterRoutersPass.php",
   "name":"symfony-cmf/routing",
   "homepage":"http://cmf.symfony.com"
}
```