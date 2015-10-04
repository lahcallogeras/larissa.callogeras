//
//  Contatos.h
//  conex
//
//  Created by Gleison on 05/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Contato : NSObject{
    
@private
    
    int codigoCtt;
    int codigoUsuLocalCtt;
    NSString *statusCtt;
    int codigoUsuCtt;
    NSString *usuarioUsuCtt;
    NSString *nomeUsuCtt;
    NSString *dataNascUsuCtt;
    NSString *emailUsuCtt;
    NSString *sexoUsuCtt;
    NSString *dataCadUsuCtt;
    NSString *nomeFotoCtt;
    int qtdMsg;
    //UIImage *fotoCtt;
    
    
    
}


@property int codigoCtt;
@property int codigoUsuLocalCtt;
@property (nonatomic, retain) NSString *statusCtt;
@property int codigoUsuCtt;
@property (nonatomic, retain) NSString *usuarioUsuCtt;
@property (nonatomic, retain) NSString *nomeUsuCtt;
@property (nonatomic, retain) NSString *dataNascUsuCtt;
@property (nonatomic, retain) NSString *emailUsuCtt;
@property (nonatomic, retain) NSString *sexoUsuCtt;
@property (nonatomic, retain) NSString *dataCadUsuCtt;
//@property (nonatomic, retain) UIImage *fotoCtt;
@property (nonatomic, retain) NSString *nomeFotoCtt;
@property int qtdMsg;
+ (Contato*)contatoGlobal;
-(void)funContatoGlobal:(Contato*)contato;



@end

