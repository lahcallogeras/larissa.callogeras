//
//  Usuario.h
//  conex
//
//  Created by Gleison on 02/11/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Usuario : NSObject{
    @private
    
	int codigoUsu;
	NSString *usuarioUsu;
    NSString *senhaUsu;
    NSString *nomeUsu;
    NSString *dataNascUsu;
    int codigoPaisUsu;
    NSString *emailUsu;
    NSString *sexoUsu;
    NSString *manterConUsu;
    NSString *dataCadUsu;
    NSString *dataLoginUsu;
	NSString *fotoUsu;
    
    
    //UIImage *fotoUsu;

}

@property int codigoUsu;
@property (nonatomic, retain) NSString *usuarioUsu;
@property (nonatomic, retain) NSString *senhaUsu;
@property (nonatomic, retain) NSString *nomeUsu;
@property (nonatomic, retain) NSString *dataNascUsu;
@property int codigoPaisUsu;
@property (nonatomic, retain) NSString *emailUsu;
@property (nonatomic, retain) NSString  *sexoUsu;
@property (nonatomic, retain) NSString  *manterConUsu;
@property (nonatomic, retain) NSString *dataCadUsu;
@property (nonatomic, retain) NSString *dataLoginUsu;
@property (nonatomic, retain) NSString *fotoUsu;
+ (Usuario*)usuarioGlobal;
-(void)funUsuarioGlobal:(Usuario*)usuario;

//@property (nonatomic) UIImage *fotoUsu;



@end


