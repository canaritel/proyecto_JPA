# proyecto_JPA

Aplicación desarrollada en Java 11 , JavaFX 11.0.2 y Maven.
Diseño de un ORM en JPA con EclipseLinkJPA 2.1.

Entorno IDE OpenBeans , http://www.openbeans.org/

Esta aplciación hace uso de un ORM con conexión a BD MySQL hosteada en Hostalia. El IDE OpenBeans nos permite facilmente crear la persistencia, Pojo entidades y métodos JPA Crud.
![image](https://user-images.githubusercontent.com/57302177/119737338-04738f80-be77-11eb-974b-5b26784395e0.png)


Base de datos compuesto por 3 tablas con relaciones de uno a muchos y de muchos a uno.
![image](https://user-images.githubusercontent.com/57302177/119736595-db063400-be75-11eb-8660-99ea45b046b5.png)

Una vez creada la persistencia el sistema incorpora en Maven las dependencias necesarias, pero es IMPORTANTE actualizar las versiones de dichas librerías a la 2.7.8:

      <dependency>
            <groupId>org.eclipse.persistence</groupId>
            <artifactId>org.eclipse.persistence.core</artifactId>
            <version>2.7.8</version>
        </dependency>
        <dependency>
            <groupId>org.eclipse.persistence</groupId>
            <artifactId>org.eclipse.persistence.asm</artifactId>
            <version>2.7.8</version>
        </dependency>
        <dependency>
            <groupId>org.eclipse.persistence</groupId>
            <artifactId>org.eclipse.persistence.antlr</artifactId>
            <version>2.7.8</version>
        </dependency>
        <dependency>
            <groupId>org.eclipse.persistence</groupId>
            <artifactId>org.eclipse.persistence.jpa</artifactId>
            <version>2.7.8</version>
        </dependency>
        <dependency>
            <groupId>org.eclipse.persistence</groupId>
            <artifactId>org.eclipse.persistence.jpa.jpql</artifactId>
            <version>2.7.8</version>
        </dependency>
        <dependency>
            <groupId>org.eclipse.persistence</groupId>
            <artifactId>javax.persistence</artifactId>
            <version>2.2.1</version>
        </dependency>
        <dependency>
            <groupId>org.eclipse.persistence</groupId>
            <artifactId>org.eclipse.persistence.jpa.modelgen.processor</artifactId>
            <version>2.7.8</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.49</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.4.31.Final</version>
            <type>jar</type>
        </dependency>
