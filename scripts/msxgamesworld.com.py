import json
from selenium import webdriver
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.proxy import Proxy, ProxyType
import time
import os


driver = webdriver.Firefox()
#driver.implicitly_wait(30)
#wait = WebDriverWait(driver, 30)

#id = 9999


'''
prox = Proxy()
prox.proxy_type = ProxyType.MANUAL
prox.http_proxy = "54.38.146.209:8080"
prox.socks_proxy = "ip_addr:port"
prox.ssl_proxy = "54.38.146.209:8080"
capabilities = webdriver.DesiredCapabilities.CHROME
prox.add_to_capabilities(capabilities)
driver = webdriver.Chrome(desired_capabilities=capabilities)


proxy = "195.80.49.10:80"



my_proxy = Proxy({'proxyType': ProxyType.MANUAL,
'httpProxy': proxy,
'sslProxy': proxy,
'noProxy': 'www.google-analytics.com, ajax.googleapis.com, apis.google.com'
})
driver=webdriver.Firefox( proxy=my_proxy)
'''


for id in range(2744,6650):
    time.sleep(3)
    data = {}

    if(os.path.exists(str(id)+'.json')):
        continue
    driver.get("https://www.msxgamesworld.com/software.php?id=" + str(id))

    body_text = driver.find_element(By.TAG_NAME,'body').text
    if 'Welcome to MSX Games World' in body_text:
        with open(str(id)+'.json', 'w') as outfile:
            outfile.write('no')
        continue

    #wait.until(EC.presence_of_element_located((By.XPATH, '//div[@class="jumbotron"]/h1')))

    data["name"] = driver.find_element(By.XPATH,"//div[@class='jumbotron']/h1").text
    name_jp = driver.find_elements(By.XPATH,"//div[@class='jumbotron']/p[1]")
    if len(name_jp) > 0:
        data["name_jp"] = name_jp[0].text
    name_alt = driver.find_elements(By.XPATH,"//div[@class='jumbotron']/p[2]")
    if len(name_alt) > 0:
        data["name_alt"] = name_alt[0].text

    elements = driver.find_elements(By.XPATH,"(//dd)")
    for i, element in enumerate(elements):
        if(element.text):
            key = element.find_element(By.XPATH,"(//dd)["+str(i+1)+"]/preceding-sibling::dt[1]").text
            if key not in data:
                data[key] = []
            data[key].append(element.text)


    data["screenshots"] = []
    screenshots = driver.find_elements(By.XPATH,"//a[contains(@href,'screenshots')]")
    for screenshot in screenshots:
        data["screenshots"].append(screenshot.get_attribute("href"))


    data["links"] = []
    links = driver.find_elements(By.XPATH,"//div[contains(text(),'Online resources')]/following-sibling::div/ul/li/child::a")
    for link in links:
        data["links"].append((link.text,link.get_attribute("href")))

    links = driver.find_elements(By.XPATH,"//div[contains(text(),'External links')]/following-sibling::div/ul/li/child::a")
    for link in links:
        data["links"].append((link.text,link.get_attribute("href")))

    data["appear"] = []
    links = driver.find_elements(By.XPATH,"//div[contains(text(),'Appears in')]/following-sibling::div/ul/li/child::a")
    for link in links:
        data["appear"].append((link.text,link.get_attribute("href")))

    data["other_systems"] = []
    links = driver.find_elements(By.XPATH,"//div[contains(text(),'Other systems versions')]/following-sibling::div/ul/li/child::a")
    for link in links:
        data["other_systems"].append((link.text,link.get_attribute("href")))

    data["series"] = []
    links = driver.find_elements(By.XPATH,"//div[contains(text(),'Series or sagas')]/following-sibling::div/ul/li/child::a")
    for link in links:
        data["series"].append((link.text,link.get_attribute("href")))

    time.sleep(1)
    driver.get("https://www.msxgamesworld.com/software-gallery.php?id=" + str(id))
    if 'Welcome to MSX Games World' not in body_text:
        #wait.until(EC.presence_of_element_located((By.XPATH, '//a[contains(@href,"images")]')))
        data["images"] = []
        screenshots = driver.find_elements(By.XPATH,"//a[contains(@href,'images')]")
        for screenshot in screenshots:
            data["images"].append(screenshot.get_attribute("href"))



    time.sleep(1)
    driver.get("https://www.msxgamesworld.com/software-releases.php?id=" + str(id))
    if 'Welcome to MSX Games World' not in body_text:
        #wait.until(EC.presence_of_element_located((By.XPATH, '//div[@class="card border-dark mb-3"]')))
        data["releases"] = {}
        elements = driver.find_elements(By.XPATH,"(//div[@class='card border-dark mb-3'])")
        for i, element in enumerate(elements):
            release = element.find_element(By.XPATH,"div[@class='card-header bg-dark text-white']").text+"_"+str(i)
            
            data["releases"][release] = {}
            sub_elements = element.find_elements(By.XPATH,"(//div[@class='card border-dark mb-3'])["+str(i+1)+"]/child::*/descendant::dd")
            for i2, sub_element in enumerate(sub_elements):
                if(sub_element.text):
                    key = sub_element.find_element(By.XPATH,"(//div[@class='card border-dark mb-3'])["+str(i+1)+"]/child::*/descendant::dd["+str(i2+1)+"]/preceding-sibling::dt[1]").text
                    if key not in data["releases"][release]:
                        data["releases"][release][key] = []
                    data["releases"][release][key].append(sub_element.text)

            data["releases"][release]["images"] = []
            sub_elements = element.find_elements(By.XPATH,"(//div[@class='card border-dark mb-3'])["+str(i+1)+"]/child::*/descendant::img")
            for i2, sub_element in enumerate(sub_elements):
                data["releases"][release]["images"].append(sub_element.get_attribute("src"))



    time.sleep(1)
    driver.get("https://www.msxgamesworld.com/software-publications.php?id=" + str(id))
    if 'Welcome to MSX Games World' not in body_text:
        #wait.until(EC.presence_of_element_located((By.XPATH, '//small')))
        data["publications"] = []
        elements = driver.find_elements(By.XPATH,"//small")
        for element in elements:
            data["publications"].append(element.text)


    json_object = json.dumps(data, indent = 4) 
    print(json_object)

    with open(str(id)+'.json', 'w') as outfile:
        json.dump(data, outfile)

    time.sleep(5)
driver.close()
