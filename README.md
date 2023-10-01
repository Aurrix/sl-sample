# State Link Pattern
The aim of state link pattern address common issues and drawbacks of MVC pattern,
improve development speed, maintainability and reduce development complexity.

## Components
In typical Model-View-Controller application, model declaration is followed by declaration of controller:
```java
// Declare a model layer
public record Todo(
        String id,
        String title,
        boolean done
) {}

// Declare controller layer
@Controller
@RequestMapping("/mvc")
public class TodoController {


    @Autowired
    TodoStoreService todoStoreService;


    @GetMapping
    public String todoHome(
            Model model
    ) {
        model.addAttribute("todos", todoStoreService.getTodos());
        return "mvc";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("todo") Todo todo,
            Model model
    ) {
        todoStoreService.addTodo(todo);
        model.addAttribute("todos", todoStoreService.getTodos());
        return "redirect:/mvc";
    }
    @PostMapping("/change-status")
    public String updateTodo(
            @ModelAttribute("todo") Todo todo,
            Model model
    ) {
        todoStoreService.updateTodo(todo);
        model.addAttribute("todos", todoStoreService.getTodos());
        return "redirect:/mvc";
    }

    @PostMapping("/delete")
    public String deleteTodo(
            @ModelAttribute("id") String id,
            Model model
    ) {
        todoStoreService.deleteTodo(id);
        model.addAttribute("todos", todoStoreService.getTodos());
        return "redirect:/mvc";
    }
}



```
State-Link pattern on the contrary attempts to omit controller layer, and rather allow
view layer directly request necessary information through a data link:
```java
@StateDefinition
@Data
public class TodoStateLink extends State {
    List<Todo> todos = new ArrayList<>();
    @Override
    public void onInit() {}

    @Override
    public void onUpdate() {}

    @Override
    public void onDestroy() {}

    @Override
    public String getKey() {
        return "todos_state";
    }
}

```
The state is an object with additional information required by State-Link Resolver.
It provides a set of common meta-data attributes for the convenience of the developers.
![classes](docs/sl-classes.png)
A typical set of components for the SL pattern therefore is as follows:
![components](docs/sl-components.png)
The request flight sequence would there look so:
![sd](docs/sl-exchange-sequence.png)
* The initial page load is resolved by either a templating engine or by static resource resolver
* The page is appended with a state link driver that acts as a medium between DOM or the client and the server
* The medium establishes connection with a state link resolver 
* The state exchange begins