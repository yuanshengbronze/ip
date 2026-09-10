# Nico

> “Your mind is for having ideas, not holding them.” — [David Allen](https://gettingthingsdone.com/)

Nico helps you keep track of your tasks so you can focus on **getting things done** instead of remembering everything. It is a *simple* task manager—no more ~~forgotten deadlines~~.

It is:

- Easy to learn
- Fast to use
- Free

All you need to do is:

1. Start Nico.
2. Add your tasks.
3. Manage them using simple commands such as `todo read book`.
4. Let Nico keep track of your progress.

Happy task managing! 😉

## Features

- [x] Manage to-do tasks
- [x] Manage tasks with deadlines
- [x] Manage events with start and end times
- [x] Mark tasks as complete or incomplete
- [x] Delete tasks
- [x] Find tasks by keyword
- [x] View urgent tasks
- [x] Automatically save and load tasks

Nico is also a Java project that can be used to practise object-oriented programming, command parsing, file storage, and exception handling. Its entry point is:

```java
public class Launcher {
    public static void main(String[] args) {
        Application.launch(Ui.class, args);
    }
}
```

## Assertions

Nico uses Java's `assert` feature to document assumptions about internal state. Assertions are not used to validate user input, because user errors must still receive the normal `NicoException` messages when assertions are disabled.

- `TaskNumberCommand` asserts that a task number which passed validation still identifies an element in the task list. This protects the indexing assumption immediately before the list access.
- `TaskStorage` asserts that the task file exists after the file-creation step and before writing. Both write methods depend on this postcondition.
- `Nico.loadTasks` asserts that every task successfully read into the temporary list is retained when replacing the chatbot's task list. This documents the intended load-and-copy invariant.
- `UrgentCommand` asserts that every selected event or deadline is incomplete and has the earliest time found. This checks the postcondition of each urgent-task search, including ties.

Assertions are enabled during development with the JVM option `-ea` (for example, `java -ea ...`).

## AI usage acknowledgement

I used AI assistance (ChatGPT Codex) throughout this project to help me implement features and debug issues. The level of usage is kept around AI-3 (Hand-code to start, get AI to finish).

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
2. Open the project into Intellij as follows:
   1. Click `Open`.
   2. Select the project directory, and click `OK`.
   3. If there are any further prompts, accept the defaults.
3. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
4. Locate `src/main/java/nico/Launcher.java`, right-click it, and choose `Run Launcher.main()` (if the code editor shows compile errors, try restarting IntelliJ IDEA). Nico will open in its own application window.

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
