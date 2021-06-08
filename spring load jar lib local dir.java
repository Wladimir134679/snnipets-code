
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableScheduling
public class RestApplication implements ApplicationContextInitializer {

    public static void main(String[] args) {
        System.out.println("Start");
        SpringApplication springApplication = new SpringApplication(RestApplication.class);
        springApplication.addInitializers(new RestApplication());
        ConfigurableApplicationContext run = springApplication.run(args);
    }
    
    
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        System.out.println("INIT!!!!!!!!!!!!!");
        List<Class<?>> classes = loadClasses(configurableApplicationContext.getBeanFactory().getBeanClassLoader());
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) configurableApplicationContext.getBeanFactory();
        for (Class<?> aClass : classes) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(aClass);
            AbstractBeanDefinition rawBeanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
            System.out.println("Bean Load: " + rawBeanDefinition.getBeanClass().getSimpleName());
            beanDefinitionRegistry.registerBeanDefinition(rawBeanDefinition.getBeanClass().getSimpleName(), rawBeanDefinition);
        }
    }

    public static List<Class<?>> loadClasses(ClassLoader classLoader){
        File dirModule = new File("./modules");
        File[] files = dirModule.listFiles();
        List<Class<?>> list = new ArrayList<>();
        if(files != null) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    if (file.getName().endsWith(".jar")) {
                        System.out.println("Find: " + file.getName());
                        try {
                            ClassLoader jarLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, classLoader);
                            JarFile jarFile = new JarFile(file);
                            Enumeration<JarEntry> entries = jarFile.entries();
                            while(entries.hasMoreElements()){
                                JarEntry jarEntry = entries.nextElement();
                                String name = jarEntry.getName();
                                System.out.println("  el: " + name);
                                if(name.endsWith(".class")){
                                    String className1 = name.substring(0, name.length() - 6);
                                    className1 = className1.replaceAll("/", ".");
                                    Class<?> aClass = jarLoader.loadClass(className1);
                                    if(aClass.getAnnotation(Service.class) != null)
                                        list.add(aClass);
                                }
                            }
                        } catch (IOException | ClassNotFoundException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
        }
        return list;
    }
}
