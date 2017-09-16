import cv2
import numpy as np
cap = cv2.VideoCapture(0)
cv2.namedWindow("Video")  
grayscale = np.zeros((480,640,1),np.uint8)
f32 = np.zeros((480,640,3),np.float32)
difference = np.zeros((480,640,3),np.uint8)

while True:
    status, img = cap.read()

    blur=cv2.GaussianBlur(img,(5,5),0)
    cv2.accumulateWeighted(blur,f32,0.32)
    #cv2.imshow("f32",f32)
    temp = cv2.convertScaleAbs(f32)
    #cv2.imshow("convert",temp)
    cv2.absdiff(temp,img,difference)
    cv2.imshow("difference",difference)
    grayscale = cv2.cvtColor(difference, cv2.COLOR_BGR2GRAY)
    retval, grayscale = cv2.threshold(grayscale,2, 255,cv2.THRESH_BINARY)
    grayscale = cv2.GaussianBlur(grayscale,(5,5),0)
    retval, grayscale = cv2.threshold(grayscale, 253, 255, cv2.THRESH_BINARY)
    cv2.imshow("grayscale",grayscale)
    (temp,cnts,_)=cv2.findContours(grayscale.copy(),cv2.RETR_EXTERNAL,cv2.CHAIN_APPROX_SIMPLE)
    
    for c in cnts:
        if cv2.contourArea(c) < 500:
            continue
        
        (x,y,w,h) = cv2.boundingRect(c)
        cv2.rectangle(img, (x,y),(x+w,y+h),(0,0,255),2)    
    
    cv2.imshow("Video", img)

    k = cv2.waitKey(1)
    if k == 27:
        break
cv2.destroyAllWindows()