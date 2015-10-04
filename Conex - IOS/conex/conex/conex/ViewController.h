//
//  ViewController.h
//  conex
//
//  Created by Gleison on 23/09/12.
//  Copyright (c) 2012 Gleison. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UsuarioDAO.h"
#import "ContatoDAO.h"
#import "MensagemDAO.h"
#import "Usuario.h"

@interface ViewController : UIViewController{

    IBOutlet UILabel *lblManterConectado;
    IBOutlet UISwitch *swtManterConectado;
    IBOutlet UITextField *txtUsuario;
    IBOutlet UITextField *txtSenha;
    IBOutlet UIButton *btnEntrar;
    IBOutlet UIButton *btnCriarConta;
    IBOutlet UIButton *btnSobre;
    IBOutlet UIView *loginView;
    IBOutlet UIView *contatosView;
    UsuarioDAO *usuarioDAO;    
}


@property (nonatomic, retain) UIView *loginView;
@property (nonatomic, retain) UIView *contatosView;
@property (nonatomic, retain) UsuarioDAO *usuarioDAO;
@property (nonatomic, retain) ContatoDAO *contatoDAO;
@property (nonatomic, retain) MensagemDAO *mensagemDAO;

- (IBAction)loginClicked:(id)sender;
- (IBAction)criarConta:(id)sender;
- (IBAction)valorSwitchAlterado:(id)sender;
- (IBAction)doneTeclado:(id)sender;
- (IBAction)ativaTeclado:(id)sender;
- (IBAction)desativaTeclado:(id)sender;
- (void) LoginOnline: (Usuario *) usuario;


@end    
