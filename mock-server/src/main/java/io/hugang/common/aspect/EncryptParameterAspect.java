package io.hugang.common.aspect;

import io.hugang.common.page.PageResult;
import io.hugang.common.utils.EncryptUtils;
import io.hugang.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import io.hugang.common.annotation.EncryptParameter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 处理参数加密解密切面
 */
@Aspect
@Slf4j
@Component
public class EncryptParameterAspect {

    /**
     * 切面方法：page、list、get、save、update、tableList
     *
     * @param proceedingJoinPoint 切点
     * @return Object
     * @throws Throwable 异常
     */
//    @Around("execution(* io.hugang.demo.simple.controller.DataSourceController.*(..))")
    @Around("execution(* io.hugang.demo.simple.*.*(..))")
    public Object doProcess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        // 处理请求入参
        List<Object> methodArgs = this.getMethodArgs(proceedingJoinPoint);
        for (Object item : methodArgs) {
            handleItem(item, true);
        }
        Object result = proceedingJoinPoint.proceed();

        // 处理返回值
        handleObject(result);
        return result;
    }

    /**
     * 获取方法的请求参数
     */
    private List<Object> getMethodArgs(ProceedingJoinPoint proceedingJoinPoint) {
        List<Object> methodArgs = new ArrayList<>();
        for (Object arg : proceedingJoinPoint.getArgs()) {
            if (Objects.nonNull(arg)) {
                methodArgs.add(arg);
            }
        }
        return methodArgs;
    }

    /**
     * 加密返回结果中的字段
     *
     * @param object 返回结果
     * @throws Exception 异常
     */
    private void handleObject(Object object) throws Exception {
        // 仅处理类型是Result的返回对象
        if (!(object instanceof Result) || Objects.isNull(((Result<?>) object).getData())) {
            return;
        }

        Object data = ((Result<?>) object).getData();
        if (data instanceof List || data instanceof PageResult) {
            List itemList = data instanceof List ? (List) data : ((PageResult<?>) data).getList();
            itemList.forEach(f ->
                    handleItem(f, false)
            );
        } else {
            handleItem(data, false);
        }
    }

    /**
     * 加密/解密具体对象下的字段
     *
     * @param item      需要加解密的对象
     * @param isDecrypt true：解密，false：加密
     */
    private void handleItem(Object item, boolean isDecrypt) {

        // 只处理在entity包下面的对象
        if (Objects.isNull(item.getClass().getPackage()) || !item.getClass().getPackage().getName().startsWith("io.hugang.demo.simple.entity")) {
            return;
        }

        // 遍历所有字段
        Field[] fields = item.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 若该字段被EncryptParameter注解,则进行解密/加密
            Class<?> fieldType = field.getType();
            if (fieldType == String.class && Objects.nonNull(AnnotationUtils.findAnnotation(field, EncryptParameter.class))) {
                // 设置private类型允许访问
                field.setAccessible(Boolean.TRUE);
                try {
                    String newFieldValue = isDecrypt ? EncryptUtils.decrypt((String) field.get(item)) : EncryptUtils.encrypt((String) field.get(item));
                    field.set(item, newFieldValue);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
                field.setAccessible(Boolean.FALSE);
            }
        }
    }

}
