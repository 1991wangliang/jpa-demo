# 为什么推荐JPA框架？ 

## 推荐理由

* 只需要创建Java类，即可完成对数据库表的维护
* 通过HQL即可实现关联查询，不需要配置复杂映射关系
* 丰富的api接口，不需要任何代码，既可实现常用操作
* 兼容各种数据库，在使用HQL的情况下可随意更换数据库
* 可支持简单快捷的分页与排序功能

## QA
1. 为什么推荐JPA?  
A: JPA的开箱即用,而且目前很多MyBatis的开发框架越来越想JPA,但是其质量又远不如JPA
2. JPA必须要配置那些复杂的OneToMany?OneToOne?ManyToMany?   
A: 不需要的，可完成通过hql既可以实现，可完全以普通建表的思路去创建Entity对象
3. JPA实现动态查询不够灵活?  
A: 本教程，在本项目中将分享6中查询的方式，复杂的场景，直接编写HQL也基本满足

## 项目介绍

本项目为JPA的演示项目，主要为呈现JPA的功能。

本项目的接口postman脚本见docs [JPA-postman](./docs/JPA.postman_collection.json)

## JPA实例教程

### 使用 JpaRepository

```java

public interface StudentRepository extends JpaRepository<Student,Integer> {
    
}

```

使用JpaRepository可以提供常用的数据库表的操作，非常齐全。

### 使用JpaRepository自定义查询

* 函数名称匹配的方式
```java

public interface StudentRepository extends JpaRepository<Student,Integer> {

    List<Student> findByNameContaining(String name,PageRequest pageRequest);
}

```
通过函数名称匹配的方式，既可完成查询功能的实现，不需要写任何SQL，而且IDEA还可以提示函数的名称。可以满足简单条件的查询功能。

* 自定义HQL的方式
```java

public interface StudentRepository extends JpaRepository<Student,Integer> {

    @Query(value = "select new com.example.jpa.demo.vo.StudentList(s.id,s.name,s.createTime,c.id,c.name) from Student s left join SchoolClass c on s.classId = c.id  where s.name like CONCAT('%',?1,'%') ")
    Page<StudentList> findByNameLike(String name, PageRequest pageRequest);
}

```
通过自定义Query既可以实现自定义SQL的查询。

### 使用Example实现动态查询

```java
// JpaRepository 接口定义
List<S> findAll(Example<S> example);
```

JpaRepository中支持通过Example的方式，实现简单条件的动态查询。
```java

    @Getter
    @Setter
    public static class FindRequest {
        private String name;


        public Example<Teacher> getExample(){
            Teacher teacher = new Teacher();
            teacher.setName(name);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.REGEX).contains())
                    .withIgnorePaths("id");
            return Example.of(teacher,matcher);
        }
    }
```
Example是支持动态查询，相对Matcher方式有限，可满足简单的动态查询要求。

### 使用Specification实现动态查询
```java
// JpaSpecificationExecutor 接口定义
List<T> findAll(@Nullable Specification<T> spec);
```

JpaSpecificationExecutor中支持通过Specification的方式，可实现复杂条件的动态查询。

```java
        @GetMapping("/find")
        public PageResponse<SchoolClass> find(ClassDTO.FindRequest request){
            Specification<SchoolClass> specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicateList = new ArrayList<>();
                if (request.haveName()) {
                    Predicate predicate = criteriaBuilder.like(
                            root.<String>get("name"), "%" + request.getName() + "%");
                    predicateList.add(predicate);
                }
                return query.where(predicateList.toArray(new Predicate[]{})).getRestriction();
            };
        classRepository.findAll(specification,request.getPageRequest(Sort.by("createTime").descending()))
        }
```
Specification是支持动态查询，相对Example，可满足更加复杂的动态查询要求。

### 使用HQL实现动态查询

在没有配置映射关系的时候，只是通过上述的Specification或Example，都只能对单表实现查询。因此在一些复杂的多表关联查询的时候，会比较麻烦。
可通过HQL，自己通过sql实现关联查询。

```java
        @GetMapping("/page3")
        public PageResponse<Student> page3(StudentDTO.PageQueryRequest request){
            StringBuilder builder = new StringBuilder();
            Map<String,Object> parameters = new HashMap<>();
            builder.append("select s from Student s where 1 = 1");
            if(request.haveName()){
                builder.append(" and s.name = :name");
                parameters.put("name",request.getName());
            }
            TypedQuery<Student> query = entityManager.createQuery(builder.toString(),Student.class);
            for(String name:parameters.keySet()){
                query.setParameter(name,parameters.get(name));
            }
            return  PageResponse.of(query.getResultList());
        }
```

