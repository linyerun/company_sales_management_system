**公司管理系统**
> req的操作
```java
String requestURI = req.getRequestURI();// 不包含baseURL
StringBuffer requestURL = req.getRequestURL();// 包含的
```
> 拦截器上淘汰的代码
```java
String[] keys = {"username", "id", "phoneNumber", "email", "identity", "employeeName"};
Map<String, Object> userMap = JwtTokenUtil.parseToken(token, keys);
if (Objects.isNull(userMap)) return false;

User user = new User();
user.setId(Long.parseLong(((Integer)userMap.get("id")).toString()));   // 好家伙，解析出来之后就不是Long类型了，人都麻了
user.setUsername((String) userMap.get("username"));
user.setPhoneNumber((String) userMap.get("phoneNumber"));
user.setEmail((String) userMap.get("email"));
user.setIdentity(Byte.parseByte(((Integer)userMap.get("id")).toString()));
user.setEmployeeName((String) userMap.get("employeeName"));
```
> json格式化返回数据相关
```java
@JsonInclude(JsonInclude.Include.NON_NULL)
```
这个注解加到类上，类上为 `null` 的属性不会被json化
> LocalDateTime相关
```java
// 必须包含时分秒
private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

public static LocalDateTime parse(String time) {
    time += " 00:00:00";
    return LocalDateTime.parse(time, fmt);
}
```
真的是难用啊

> 鉴权大漏洞
```java
if (!user.getIdentity().equals((byte) 2)) //必须得这样进行判断，不然都是false
```
> swagger渲染的实体类的类名千万不要相同，不然会出现一个被另一个覆盖导致上传参数的时候有问题

> 可以通过updateById完成非零值更新，零值不会覆盖原值
```java
Contract c = new Contract();
c.setId(contractId);
c.setTotalAmount(contract.getTotalAmount().add(new BigDecimal(purchasingGoods.getPurchasingCount()).multiply(goods.getUnitPrice())));
val = contractMapper.updateById(c);
if (val != 1) {
    throw new Exception("500;删除失败");
}
```

> mysql的decimal(10,5)
这个精度就占5位，所以我们的整数部分最多就是5位了。这个一定要注意
