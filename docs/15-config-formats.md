# Module 15: Configuration Formats (YAML vs. Properties)

## 1. Why YAML?
- **Hierarchical:** It groups related settings together (e.g., all `spring.datasource` settings are nested).
- **Readability:** It uses less repetitive text than `.properties` files.
- **Profiles:** It makes it much easier to manage different environments (Dev, Test, Prod) in a single file using `---` separators.

## 2. Common Pitfalls
- **Indentation:** YAML is whitespace-sensitive. You must use spaces, not tabs.
- **Colon Spacing:** There must be a space after the colon (e.g., `key: value`, not `key:value`).

## 3. Interview Talking Point
"I chose YAML for configuration because of its readability and hierarchical structure. It allows for better organization of complex properties, especially when managing multiple profiles or complex JPA settings."