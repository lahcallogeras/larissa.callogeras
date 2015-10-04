//
//  Mensagem.h
//  conex
//
//  Created by Larii on 10/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Contato.h"

@interface Mensagem : NSObject{
    
@private
    
    int codigoMsg;
    Contato *usuOrig;
    Contato *usuDestino;
    NSString *dataMsg;
    NSString *textoMsg;
    NSString *statusLida;
}

@property int codigoMsg;
@property (nonatomic, retain) Contato *usuOrig;
@property (nonatomic, retain) Contato *usuDestino;
@property (nonatomic, retain) NSString *dataMsg;
@property (nonatomic, retain) NSString *textoMsg;
@property (nonatomic, retain) NSString *statusLida;

@end