# Nico project

This is a greenfield Java project for the _Nico_ chatbot. Given below are instructions on how to use it.

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
4. After that, locate the `src/main/java/Nico.java` file, right-click it, and choose `Run Nico.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
   ____________________________________________________________
   ███╗   ██╗██╗ ██████╗ ██████╗
   ████╗  ██║██║██╔════╝██╔═══██╗
   ██╔██╗ ██║██║██║     ██║   ██║
   ██║╚██╗██║██║██║     ██║   ██║
   ██║ ╚████║██║╚██████╗╚██████╔╝
   ╚═╝  ╚═══╝╚═╝ ╚═════╝ ╚═════╝
   
   Hey man! It's Nico, what can I do for you?
   ____________________________________________________________
   Nice seeing you. Until next time!
   ____________________________________________________________
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
