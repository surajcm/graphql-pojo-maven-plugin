<h1 align="center">
    graphql-pojo-maven-plugin (Work In Progress)
</h1>
<p align="center">
    <a href="https://github.com/surajcm/graphql-pojo-maven-plugin/commits/" title="Last Commit"><img src="https://img.shields.io/github/last-commit/surajcm/graphql-pojo-maven-plugin?style=flat"></a>
    <a href="https://github.com/surajcm/graphql-pojo-maven-plugin/issues" title="Open Issues"><img src="https://img.shields.io/github/issues/surajcm/graphql-pojo-maven-plugin?style=flat"></a>
    <a href="https://github.com/surajcm/graphql-pojo-maven-plugin/actions/workflows/test.yml" title="Tests"><img src="https://github.com/surajcm/graphql-pojo-maven-plugin/actions/workflows/build.yml/badge.svg"></a>
    <a href="https://github.com/surajcm/graphql-pojo-maven-plugin/blob/master/LICENSE" title="License"><img src="https://img.shields.io/badge/License-MIT-green.svg?style=flat"></a>
    <a href="https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg" title="code_of_conduct.md"><img src="https://img.shields.io/badge/Contributor%20Covenant-2.1-4baaaa.svg"></a>
    <a href="https://github.com/surajcm/graphql-pojo-maven-plugin/pulls?q=is%3Apr+is%3Amerged+created%3A2022-10-01..2022-10-31" title="Hacktoberfest 2022 stats"><img src="https://img.shields.io/github/hacktoberfest/2022/surajcm/graphql-pojo-maven-plugin?label=Hacktoberfest+2022"></a>
</p>
<a href="https://foojay.io/today/works-with-openjdk"><img align="right" src="https://github.com/foojayio/badges/raw/main/works_with_openjdk/Works-with-OpenJDK.png" width="100"></a>
<hr />
A simple graphql schema to pojo generator for java. Doesn't depend on any other graphql / java frameworks.

## Author

👤 **Suraj Muraleedharan**

* Github: [@surajcm](https://github.com/surajcm)

## 🤝 Contributing

Contributions, issues and feature requests are welcome!<br />Feel free to check [issues page](https://github.com/surajcm/graphql-pojo-maven-plugin/issues).
If you contribute please commit to a new branch and explain details in your pull request not in your commit.

Remember to abide by code of conduct , which we adapted from ![Contributor Covenant 1.3](https://img.shields.io/badge/Contributor%20Covenant-1.3-4baaaa.svg) [Code of Conduct](CODE_OF_CONDUCT.md)
## Show your support

Give a ⭐️ if this project helped you!

### Plugin Configurations

```xml
<build>
    <plugins>
        ...
        <plugin>
            <groupId>com.github.surajcm</groupId>
            <artifactId>graphql-pojo-maven-plugin</artifactId>
            <version>1.0.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                    <configuration>
                        <schema>src/main/resources/schema.graphqls</schema>
                        <outputDir>${project.build.directory}/src/main/java/</outputDir>
                        <packageName>com.company.project.schema</packageName>
                    </configuration>
                </execution>
            </executions>
        </plugin>
        ...
    </plugins>
</build>
```
<sub>* Both `schema` and `outputDir` are optional, and they default to the values provided in the sample above</sub>

The plugin will run automatically as part of the maven lifecycle when compiling your code , also you can run the plugin manually with `mvn generate-sources`.
