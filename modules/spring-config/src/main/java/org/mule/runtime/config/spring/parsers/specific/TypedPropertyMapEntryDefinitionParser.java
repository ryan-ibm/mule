/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.config.spring.parsers.specific;

import static org.mule.runtime.config.spring.parsers.specific.DataTypeFactoryBean.buildDataTypeDefinition;

import org.mule.runtime.config.spring.parsers.assembly.BeanAssembler;
import org.mule.runtime.config.spring.parsers.assembly.BeanAssemblerFactory;
import org.mule.runtime.config.spring.parsers.assembly.DefaultBeanAssembler;
import org.mule.runtime.config.spring.parsers.assembly.configuration.PropertyConfiguration;
import org.mule.runtime.config.spring.parsers.collection.ChildMapEntryDefinitionParser;
import org.mule.runtime.core.metadata.TypedValue;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;

public class TypedPropertyMapEntryDefinitionParser extends ChildMapEntryDefinitionParser {

  public TypedPropertyMapEntryDefinitionParser(String mapName) {
    super(mapName);
    setBeanAssemblerFactory(new TypedPropertyMapEntryBeanAssemblerFactory());
  }

  private static class TypedPropertyMapEntryBeanAssemblerFactory implements BeanAssemblerFactory {

    @Override
    public BeanAssembler newBeanAssembler(PropertyConfiguration beanConfig, BeanDefinitionBuilder bean,
                                          PropertyConfiguration targetConfig, BeanDefinition target) {
      return new TypedPropertyMapEntryBeanAssembler(beanConfig, bean, targetConfig, target);
    }
  }

  private static class TypedPropertyMapEntryBeanAssembler extends DefaultBeanAssembler {

    public static final String VALUE_REF = "value-ref";

    public TypedPropertyMapEntryBeanAssembler(PropertyConfiguration beanConfig, BeanDefinitionBuilder bean,
                                              PropertyConfiguration targetConfig, BeanDefinition target) {
      super(beanConfig, bean, targetConfig, target);
    }

    @Override
    public void insertBeanInTarget(String oldName) {
      assertTargetPresent();
      PropertyValues sourceProperties = bean.getRawBeanDefinition().getPropertyValues();
      String newName = bestGuessName(targetConfig, oldName, target.getBeanClassName());
      MutablePropertyValues targetProperties = target.getPropertyValues();
      PropertyValue propertyValue = targetProperties.getPropertyValue(newName);
      @SuppressWarnings("unchecked")
      ManagedMap<String, Object> propertiesMap =
          (ManagedMap<String, Object>) (null == propertyValue ? null : propertyValue.getValue());

      if (propertiesMap == null) {
        propertiesMap = new ManagedMap<>();
        propertyValue = new PropertyValue(newName, propertiesMap);
        targetProperties.addPropertyValue(propertyValue);
      }

      PropertyValue propertyKey = sourceProperties.getPropertyValue(ChildMapEntryDefinitionParser.KEY);

      AbstractBeanDefinition typedValueBeanDefinition = getTypedValue(sourceProperties);

      propertiesMap.put((String) propertyKey.getValue(), typedValueBeanDefinition);
    }

    private AbstractBeanDefinition getTypedValue(PropertyValues sourceProperties) {
      AbstractBeanDefinition dataType = buildDataTypeDefinition(Object.class.getName(), sourceProperties);

      BeanDefinitionBuilder typedValueBeanDefinition = BeanDefinitionBuilder.genericBeanDefinition(TypedValue.class);
      if (sourceProperties.contains(VALUE_REF)) {
        String valueRef = (String) sourceProperties.getPropertyValue(VALUE_REF).getValue();

        typedValueBeanDefinition.addConstructorArgReference(valueRef);
      } else {
        Object value = sourceProperties.getPropertyValue(ChildMapEntryDefinitionParser.VALUE).getValue();
        typedValueBeanDefinition.addConstructorArgValue(value);
      }

      typedValueBeanDefinition.addConstructorArgValue(dataType);

      return typedValueBeanDefinition.getBeanDefinition();
    }
  }

}
