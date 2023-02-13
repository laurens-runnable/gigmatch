# Website consumer

## Run app

```bash
# Run application
dotnet run
# In watch mode
dotnet watch
```

## Generate event models

Code generation is not integrated in the build process and has to be invoked manually.

```bash
# Generate event model code
dotnet run generate-model SkillCreatedOrUpdated
Writing ./Events/SkillCreatedOrUpdated.cs
```

Generated code must be committed to git.
