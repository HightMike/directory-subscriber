import React from 'react'
import InputMask from 'react-input-mask'
import './FormNewParticipants.css'
import logo from './images/logo.png'
import axios from 'axios'

export const PHONE_NUMBER = /^\d{3}-?\d{3}-?\d{2}-?\d{2}$/

class FormNewParticipants extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            firstName: '',
            lastName: '',
            workPhone: '',
            mobile: '',
            email: '',

            firstNameValid: true,
            lastNameValid: true,
            workPhoneValid: true,
            emailValid: true,
            mobileValid: true,

            firstNameValidationErrors: '',
            lastNameValidationErrors: '',
            workPhoneValidationErrors: '',
            emailValidationErrors: '',
            mobailValidationErrors: '',

            messageAboutPrepare: '',
            messageAboutMove: '',
        }
    }

    handleSubmit = (e) => {
        e.preventDefault()

        const {firstName, lastName, workPhone, mobile, email, firstNameValidationErrors, lastNameValidationErrors, workPhoneValidationErrors} = this.state

        if (firstName.length === 0) {
            this.setState({
                firstNameValidationErrors: 'Поле обязательно к заполнению',
            })
        }
        if (lastName.length === 0) {
            this.setState({
                lastNameValidationErrors: 'Поле обязательно к заполнению',
            })
        }
        if (workPhone.length === 0) {
            this.setState({
                workPhoneValidationErrors: 'Поле обязательно к заполнению',
            })
        }

        if (firstName.length > 0 && lastName.length > 0 && workPhone.length > 0 && !firstNameValidationErrors &&
            !lastNameValidationErrors && !workPhoneValidationErrors) {

            axios({
                method: 'post',
                headers: {'Content-Type': 'application/json'},
                url: '/data/prepare',
                data: {
                    firstName: firstName,
                    lastName: lastName,
                    workPhone: workPhone,
                    mobile: mobile,
                    email: email,
                },
            }).then((response) => {
                const {resultDescription} = response.data
                this.setState({
                    messageAboutPrepare: resultDescription,
                })
            }).then((error) => {
                console.log(error)
            })
        }
    }

    handleChange = (fieldName, e) => {
        this.setState({
            [`${fieldName}ValidationErrors`]: '',
            [fieldName]: e.target.value,
        })
    }

    validateField = (fieldName, e) => {
        const {value} = e.target
        this.checkValidation({
            fieldName,
            value: value,
            [`${fieldName}Valid`]: this.state[`${fieldName}Valid`],
        })
    }

    checkValidation = ({fieldName, value, firstNameValid, lastNameValid, workPhoneValid, mobileValid, emailValid}) => {
        switch (fieldName) {
            case 'firstName':
                firstNameValid = value.length <= 10
                let firstNameValidationErrors = firstNameValid ? '' : 'Должно быть меньше 10 символов'
                this.setState({
                    firstNameValidationErrors: firstNameValidationErrors,
                })
                break

            case 'lastName':
                lastNameValid = value.length <= 20
                let lastNameValidationErrors = lastNameValid ? '' : 'Должно быть меньше 20 символов'

                this.setState({
                    lastNameValidationErrors: lastNameValidationErrors,
                })
                break

            case 'workPhone':
                workPhoneValid = PHONE_NUMBER.test(value)
                let workPhoneValidationErrors = workPhoneValid ? '' : 'Поле формата «nnn-xxx-xx-xx»'

                this.setState({
                    workPhoneValidationErrors: workPhoneValidationErrors,
                })
                break

            case 'mobile':
                mobileValid = PHONE_NUMBER.test(value)
                let mobailValidationErrors = mobileValid ? '' : 'Поле формата «nnn-xxx-xx-xx»'

                this.setState({
                    mobailValidationErrors: mobailValidationErrors,
                })
                break

            case 'email':
                emailValid = value.match(/^([\w\.\-]+)@([\w\-]+)$/i)
                let emailValidationErrors = emailValid ? '' : 'Поле формата «nnn-xxx-xx-xx»'

                this.setState({
                    emailValidationErrors: emailValidationErrors,
                })
                break
        }
    }

    clearForm = () => {
        this.setState({
            firstName: '',
            lastName: '',
            workPhone: '',
            mobile: '',
            email: '',

            firstNameValid: true,
            lastNameValid: true,
            workPhoneValid: true,
            emailValid: true,
            mobileValid: true,

            firstNameValidationErrors: '',
            lastNameValidationErrors: '',
            workPhoneValidationErrors: '',
            emailValidationErrors: '',
            mobailValidationErrors: '',
        })
    }

    sendData = () => {
        const headers = {
            'Content-Type': 'application/json',
        }

        axios.put(`/data/move`, {headers})
            .then(response => {
                const {resultDescription} = response.data
                this.setState({
                    messageAboutMove: resultDescription,
                })
            })
            .then((error) => {
                console.log(error)
            })
    }

    render() {
        const {
            firstName,
            lastName,
            workPhone,
            mobile,
            email,

            firstNameValidationErrors,
            lastNameValidationErrors,
            workPhoneValidationErrors,
            emailValidationErrors,
            mobailValidationErrors,
            messageAboutPrepare,
            messageAboutMove,
        } = this.state

        return (
            <>
                <section className="logo">
                    <img src={logo} width="203px" height="51px" className="logo__img" alt="logo"/>
                </section>
                <div className="block"></div>

                <div className="wrapper">
                    <form className="form" onSubmit={this.handleSubmit}>
                        <div className="form__content">
                            <label htmlFor="firstName">Имя</label>
                            <input
                                type="text"
                                className="form__input"
                                id="firstName"
                                value={firstName}
                                onChange={(e) => this.handleChange('firstName', e)}
                                onBlur={(e) => this.validateField('firstName', e)}
                            />
                            {firstNameValidationErrors && firstNameValidationErrors &&
                            <span className="form__input-error">{firstNameValidationErrors}</span>}
                        </div>
                        <div className="form__content">
                            <label htmlFor="lastName">Фамилия</label>
                            <input
                                type="text"
                                className="form__input"
                                id="lastName"
                                value={lastName}
                                onChange={(e) => this.handleChange('lastName', e)}
                                onBlur={(e) => this.validateField('lastName', e)}
                            />
                            {lastNameValidationErrors && lastNameValidationErrors &&
                            <span className="form__input-error">{lastNameValidationErrors}</span>}
                        </div>
                        <div className="form__content">
                            <label htmlFor="workPhone">Рабочий тел.</label>
                            <InputMask
                                mask="999-999-99-99"
                                className="form__input"
                                id="workPhone"
                                value={workPhone}
                                onChange={(e) => this.handleChange('workPhone', e)}
                                onBlur={(e) => this.validateField('workPhone', e)}
                            />
                            {workPhoneValidationErrors && workPhoneValidationErrors &&
                            <span className="form__input-error">{workPhoneValidationErrors}</span>}
                        </div>
                        <div className="form__content">
                            <label htmlFor="mobile">Мобильный тел.</label>
                            <InputMask
                                mask="999-999-99-99"
                                className="form__input"
                                id="mobile"
                                value={mobile}
                                onChange={(e) => this.handleChange('mobile', e)}
                                onBlur={(e) => this.validateField('mobile', e)}
                            />
                            {mobailValidationErrors && mobailValidationErrors &&
                            <span className="form__input-error">{mobailValidationErrors}</span>}
                        </div>
                        <div className="form__content">
                            <label htmlFor="email">Email</label>
                            <input
                                type="text"
                                className="form__input"
                                id="email"
                                value={email}
                                onChange={(e) => this.handleChange('email', e)}
                                onBlur={(e) => this.validateField('email', e)}
                            />
                            {email.length && emailValidationErrors && emailValidationErrors ?
                                <span className="form__input-error">{emailValidationErrors}</span> : ''}
                        </div>
                        <div className="form__content form__btn">
                            <button type="submit" className="form__button">Добавить</button>
                            <button type="button" onClick={this.clearForm} className="form__button">Очистить</button>
                            <button type="button" onClick={this.sendData} className="form__button">Отправить</button>
                        </div>
                    </form>
                </div>

                {messageAboutPrepare && <div className={messageAboutPrepare ? 'notification__show' : 'notification'}>{messageAboutPrepare}</div>}
                {messageAboutMove && <div className={messageAboutMove ? 'notification__show' : 'notification'}>{messageAboutMove}</div>}
            </>
        )
    }
}

export default FormNewParticipants
